package net.luckyowlstudios.wardrobe.mixin;

import net.luckyowlstudios.wardrobe.cosmetic.CosmeticHolder;
import net.luckyowlstudios.wardrobe.network.WardrobeNetworking;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    /** Flush any cosmetic changes from this tick to the player and everyone watching them. */
    @Inject(method = "tick", at = @At("TAIL"))
    private void luckys_wardrobe$syncCosmetics(CallbackInfo ci) {
        ServerPlayer self = (ServerPlayer) (Object) this;
        if (CosmeticHolder.get(self).consumeDirty()) {
            WardrobeNetworking.broadcast(self);
        }
    }

    /** Cosmetics are kept through death and when returning from the End. */
    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void luckys_wardrobe$keepCosmetics(ServerPlayer oldPlayer, boolean keepEverything, CallbackInfo ci) {
        CosmeticHolder.get((ServerPlayer) (Object) this).copyFrom(CosmeticHolder.get(oldPlayer));
    }
}
