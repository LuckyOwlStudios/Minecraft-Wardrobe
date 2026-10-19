package net.luckyowlstudios.wardrobe.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.luckyowlstudios.wardrobe.Constants;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Extra 3D models a cosmetic can ask for (sleeves, hoods, capes, or a fully custom model). They follow the player's
 * pose, so a model's cubes go in the part they should move with.
 *
 * <p>To add one: export the model from Blockbench as a Mojang-mapped Java entity model, keep the
 * {@code createBodyLayer()} method, and make sure it has the parts {@code head}, {@code hat}, {@code body},
 * {@code right_arm}, {@code left_arm}, {@code right_leg} and {@code left_leg} (empty is fine; {@link #humanoidMesh()}
 * gives you all of them with the player's pivots). Then call {@link #register} from client setup and add the id to a
 * cosmetic with {@code CosmeticProperties.Builder#extraModel}.
 *
 * <p>A model can also have a dye overlay texture. It is drawn over the base texture, tinted with the item's dye color
 * (or untinted when undyed), the same way leather armor works. Add the item to the {@code minecraft:dyeable} tag so
 * it can be dyed in a crafting grid.
 */
public final class CosmeticModels {

    private static final Map<ResourceLocation, Entry> MODELS = new HashMap<>();
    private static final Set<ResourceLocation> WARNED = new HashSet<>();

    public static void register(ResourceLocation id, Supplier<LayerDefinition> layer, ResourceLocation texture) {
        register(id, layer, texture, null);
    }

    public static void register(ResourceLocation id, Supplier<LayerDefinition> layer, ResourceLocation texture,
                                @Nullable ResourceLocation dyeOverlayTexture) {
        MODELS.put(id, new Entry(layer, texture, dyeOverlayTexture));
    }

    /** A mesh with the empty humanoid parts every extra model needs, pivoted where the player's parts are. */
    public static MeshDefinition humanoidMesh() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5.0F, 2.0F, 0.0F));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(5.0F, 2.0F, 0.0F));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-1.9F, 12.0F, 0.0F));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(1.9F, 12.0F, 0.0F));
        return mesh;
    }

    static void render(ResourceLocation id, ItemStack stack, PlayerModel<AbstractClientPlayer> parent, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int overlay) {
        Entry entry = MODELS.get(id);
        if (entry == null) {
            if (WARNED.add(id)) {
                Constants.LOG.warn("Cosmetic asked for extra model {} but none is registered", id);
            }
            return;
        }
        HumanoidModel<AbstractClientPlayer> model = entry.model();
        parent.copyPropertiesTo(model);
        model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucent(entry.texture)), packedLight, overlay, -1);
        if (entry.dyeOverlayTexture != null) {
            int color = DyedItemColor.getOrDefault(stack, -1);
            model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucent(entry.dyeOverlayTexture)), packedLight, overlay, color);
        }
    }

    private static final class Entry {
        private final Supplier<LayerDefinition> layer;
        private final ResourceLocation texture;
        @Nullable
        private final ResourceLocation dyeOverlayTexture;
        private HumanoidModel<AbstractClientPlayer> model;

        private Entry(Supplier<LayerDefinition> layer, ResourceLocation texture, @Nullable ResourceLocation dyeOverlayTexture) {
            this.layer = layer;
            this.texture = texture;
            this.dyeOverlayTexture = dyeOverlayTexture;
        }

        private HumanoidModel<AbstractClientPlayer> model() {
            if (model == null) {
                model = new HumanoidModel<>(layer.get().bakeRoot());
            }
            return model;
        }
    }

    private CosmeticModels() {
    }
}
