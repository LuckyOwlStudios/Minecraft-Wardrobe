package net.luckyowlstudios.wardrobe.cosmetic;

import net.minecraft.world.entity.player.Player;

/** Implemented on {@link Player} by {@code PlayerMixin}. */
public interface CosmeticHolder {

    CosmeticInventory luckys_wardrobe$getCosmetics();

    static CosmeticInventory get(Player player) {
        return ((CosmeticHolder) player).luckys_wardrobe$getCosmetics();
    }
}
