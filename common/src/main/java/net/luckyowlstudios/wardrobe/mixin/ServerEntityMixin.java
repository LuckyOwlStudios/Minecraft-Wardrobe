package net.luckyowlstudios.wardrobe.mixin;

import net.luckyowlstudios.wardrobe.network.WardrobeNetworking;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntity.class)
public abstract class ServerEntityMixin {

    @Shadow
    @Final
    private Entity entity;

    /** When someone starts seeing a player, send them what that player is wearing. */
    @Inject(method = "addPairing", at = @At("TAIL"))
    private void luckys_wardrobe$sendCosmetics(ServerPlayer viewer, CallbackInfo ci) {
        if (entity instanceof Player target) {
            WardrobeNetworking.sendTo(viewer, target);
        }
    }
}
