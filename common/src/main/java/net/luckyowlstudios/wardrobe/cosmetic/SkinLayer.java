package net.luckyowlstudios.wardrobe.cosmetic;

/**
 * The outer ("second") layers of a player skin. A cosmetic can hide any of these so the player's own overlay (hair on
 * the hat layer, a jacket, etc.) doesn't poke through the clothing painted on top of their skin.
 */
public enum SkinLayer {
    HAT,
    JACKET,
    LEFT_SLEEVE,
    RIGHT_SLEEVE,
    LEFT_PANTS,
    RIGHT_PANTS
}
