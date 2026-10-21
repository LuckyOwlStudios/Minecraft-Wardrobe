package net.luckyowlstudios.wardrobe.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.luckyowlstudios.wardrobe.cosmetic.CosmeticHolder;
import net.luckyowlstudios.wardrobe.cosmetic.CosmeticInventory;
import net.luckyowlstudios.wardrobe.cosmetic.CosmeticItem;
import net.luckyowlstudios.wardrobe.cosmetic.CosmeticSlot;
import net.luckyowlstudios.wardrobe.cosmetic.SkinLayer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Cosmetics are drawn with the player's own model geometry, so a shirt sits exactly on the skin rather than on an
 * inflated armor-style shell. Its base-layer pixels paint over the skin; its outer-layer pixels use the skin's
 * extruded second layer.
 */
public final class CosmeticRendering {

    private static final SkinLayer[] SKIN_LAYERS = SkinLayer.values();
    private static final Map<CosmeticItem, ResourceLocation[]> TEXTURES = new HashMap<>();

    public static ModelPart part(PlayerModel<?> model, SkinLayer layer) {
        return switch (layer) {
            case HAT -> model.hat;
            case JACKET -> model.jacket;
            case LEFT_SLEEVE -> model.leftSleeve;
            case RIGHT_SLEEVE -> model.rightSleeve;
            case LEFT_PANTS -> model.leftPants;
            case RIGHT_PANTS -> model.rightPants;
        };
    }

    /** Runs after vanilla sets which skin layers are visible, before the player is drawn. */
    public static void hideCoveredSkinLayers(AbstractClientPlayer player, PlayerModel<?> model) {
        if (player.isSpectator()) {
            return;
        }
        for (SkinLayer layer : CosmeticHolder.get(player).hiddenSkinLayers()) {
            part(model, layer).visible = false;
        }
    }

    /**
     * The skin-layout texture for a cosmetic: {@code <namespace>:textures/cosmetic/<item>.png}, or
     * {@code <item>_slim.png} for slim-armed players when the cosmetic has a slim variant.
     */
    public static ResourceLocation skinTexture(CosmeticItem item, boolean slim) {
        ResourceLocation[] textures = TEXTURES.computeIfAbsent(item, key -> {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(key);
            String base = "textures/cosmetic/" + id.getPath();
            ResourceLocation wide = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), base + ".png");
            ResourceLocation slimTexture = key.cosmetic().slimVariant()
                    ? ResourceLocation.fromNamespaceAndPath(id.getNamespace(), base + "_slim.png")
                    : wide;
            return new ResourceLocation[]{wide, slimTexture};
        });
        return textures[slim ? 1 : 0];
    }

    public static boolean isSlim(AbstractClientPlayer player) {
        return player.getSkin().model() == PlayerSkin.Model.SLIM;
    }

    /** Makes every outer layer visible so cosmetics can draw on them, returning the previous state to restore. */
    static boolean[] showAllSkinLayers(PlayerModel<?> model) {
        boolean[] previous = new boolean[SKIN_LAYERS.length];
        for (SkinLayer layer : SKIN_LAYERS) {
            ModelPart part = part(model, layer);
            previous[layer.ordinal()] = part.visible;
            part.visible = true;
        }
        return previous;
    }

    static void restoreSkinLayers(PlayerModel<?> model, boolean[] previous) {
        for (SkinLayer layer : SKIN_LAYERS) {
            part(model, layer).visible = previous[layer.ordinal()];
        }
    }

    public static void renderFirstPersonArm(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                            AbstractClientPlayer player, ModelPart arm, ModelPart sleeve) {
        CosmeticInventory cosmetics = CosmeticHolder.get(player);
        if (cosmetics.isEmpty()) {
            return;
        }
        boolean slim = isSlim(player);
        boolean sleeveVisible = sleeve.visible;
        sleeve.visible = true;
        for (CosmeticSlot slot : CosmeticSlot.RENDER_ORDER) {
            ItemStack stack = cosmetics.get(slot);
            if (stack.getItem() instanceof CosmeticItem item && item.cosmetic().paintsSkin()) {
                RenderType renderType = RenderType.entityTranslucent(skinTexture(item, slim));
                arm.render(poseStack, buffer.getBuffer(renderType), packedLight, OverlayTexture.NO_OVERLAY);
                sleeve.render(poseStack, buffer.getBuffer(renderType), packedLight, OverlayTexture.NO_OVERLAY);
            }
        }
        sleeve.visible = sleeveVisible;
    }

    private CosmeticRendering() {
    }
}
