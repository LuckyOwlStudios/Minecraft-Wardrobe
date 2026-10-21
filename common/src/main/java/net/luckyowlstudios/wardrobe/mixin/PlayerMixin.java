package net.luckyowlstudios.wardrobe.mixin;

import net.luckyowlstudios.wardrobe.cosmetic.CosmeticHolder;
import net.luckyowlstudios.wardrobe.cosmetic.CosmeticInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Gives every player (server and client side) their cosmetic slots, and saves them with the player. */
@Mixin(Player.class)
public abstract class PlayerMixin implements CosmeticHolder {

    @Unique
    private static final String luckys_wardrobe$SAVE_KEY = "luckys_wardrobe:cosmetics";

    @Unique
    private final CosmeticInventory luckys_wardrobe$cosmetics = new CosmeticInventory();

    @Override
    public CosmeticInventory luckys_wardrobe$getCosmetics() {
        return luckys_wardrobe$cosmetics;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void luckys_wardrobe$saveCosmetics(CompoundTag tag, CallbackInfo ci) {
        Player self = (Player) (Object) this;
        tag.put(luckys_wardrobe$SAVE_KEY, luckys_wardrobe$cosmetics.save(self.registryAccess()));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void luckys_wardrobe$loadCosmetics(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains(luckys_wardrobe$SAVE_KEY, Tag.TAG_COMPOUND)) {
            Player self = (Player) (Object) this;
            luckys_wardrobe$cosmetics.load(tag.getCompound(luckys_wardrobe$SAVE_KEY), self.registryAccess());
        }
    }
}
