package net.luckyowlstudios.wardrobe.mixin;

import net.luckyowlstudios.wardrobe.cosmetic.CosmeticHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    /**
     * Runs on join, respawn and dimension change. The client recreates its player entity in each case, so resend
     * the player's own cosmetics.
     */
    @Inject(method = "sendLevelInfo", at = @At("TAIL"))
    private void luckys_wardrobe$resyncCosmetics(ServerPlayer player, ServerLevel level, CallbackInfo ci) {
        CosmeticHolder.get(player).markDirty();
    }
}
