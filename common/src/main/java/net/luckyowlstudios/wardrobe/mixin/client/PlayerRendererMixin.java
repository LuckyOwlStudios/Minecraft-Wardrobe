package net.luckyowlstudios.wardrobe.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.luckyowlstudios.wardrobe.client.render.CosmeticLayer;
import net.luckyowlstudios.wardrobe.client.render.CosmeticRendering;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void luckys_wardrobe$addCosmeticLayer(EntityRendererProvider.Context context, boolean slim, CallbackInfo ci) {
        this.addLayer(new CosmeticLayer(this));
    }

    /** Hide the parts of the player's own skin overlay that their cosmetics cover. */
    @Inject(method = "setModelProperties", at = @At("TAIL"))
    private void luckys_wardrobe$hideCoveredSkinLayers(AbstractClientPlayer player, CallbackInfo ci) {
        CosmeticRendering.hideCoveredSkinLayers(player, this.getModel());
    }

    /** Paint cosmetics onto the first-person arm too. */
    @Inject(method = "renderHand", at = @At("TAIL"))
    private void luckys_wardrobe$renderCosmeticHand(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                                    AbstractClientPlayer player, ModelPart arm, ModelPart sleeve, CallbackInfo ci) {
        CosmeticRendering.renderFirstPersonArm(poseStack, buffer, packedLight, player, arm, sleeve);
    }
}
