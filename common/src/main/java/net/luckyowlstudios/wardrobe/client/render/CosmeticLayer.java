package net.luckyowlstudios.wardrobe.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.luckyowlstudios.wardrobe.cosmetic.CosmeticHolder;
import net.luckyowlstudios.wardrobe.cosmetic.CosmeticInventory;
import net.luckyowlstudios.wardrobe.cosmetic.CosmeticItem;
import net.luckyowlstudios.wardrobe.cosmetic.CosmeticSlot;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

/** Draws worn cosmetics on the player. Added to every player renderer by {@code PlayerRendererMixin}. */
public class CosmeticLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public CosmeticLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player,
                       float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw,
                       float headPitch) {
        if (player.isInvisible() || player.isSpectator()) {
            return;
        }
        CosmeticInventory cosmetics = CosmeticHolder.get(player);
        if (cosmetics.isEmpty()) {
            return;
        }
        PlayerModel<AbstractClientPlayer> model = getParentModel();
        int overlay = LivingEntityRenderer.getOverlayCoords(player, 0.0F);
        boolean slim = CosmeticRendering.isSlim(player);
        boolean wearingHelmet = !player.getItemBySlot(EquipmentSlot.HEAD).isEmpty();

        // The player's own overlay may be hidden, but the cosmetic's second layer still needs those parts.
        boolean[] skinLayers = CosmeticRendering.showAllSkinLayers(model);
        for (CosmeticSlot slot : CosmeticSlot.RENDER_ORDER) {
            ItemStack stack = cosmetics.get(slot);
            if (!(stack.getItem() instanceof CosmeticItem item)) {
                continue;
            }
            if (item.cosmetic().paintsSkin()) {
                RenderType renderType = RenderType.entityTranslucent(CosmeticRendering.skinTexture(item, slim));
                model.renderToBuffer(poseStack, buffer.getBuffer(renderType), packedLight, overlay, -1);
            }
            float headLift = wearingHelmet ? item.cosmetic().helmetLift() : 0.0F;
            for (ResourceLocation modelId : item.cosmetic().extraModels()) {
                CosmeticModels.render(modelId, stack, model, headLift, poseStack, buffer, packedLight, overlay);
            }
        }
        CosmeticRendering.restoreSkinLayers(model, skinLayers);
    }
}
