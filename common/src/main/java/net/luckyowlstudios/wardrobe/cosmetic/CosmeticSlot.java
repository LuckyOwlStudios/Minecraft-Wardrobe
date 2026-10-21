package net.luckyowlstudios.wardrobe.cosmetic;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * The cosmetic slots every player has. The ordinal is the slot's index in {@link CosmeticInventory}, so new slots must
 * only ever be appended to keep saved data valid.
 */
public enum CosmeticSlot {
    HAT("hat", EnumSet.noneOf(SkinLayer.class)),
    SHIRT("shirt", EnumSet.of(SkinLayer.JACKET, SkinLayer.LEFT_SLEEVE, SkinLayer.RIGHT_SLEEVE)),
    PANTS("pants", EnumSet.of(SkinLayer.LEFT_PANTS, SkinLayer.RIGHT_PANTS)),
    SHOES("shoes", EnumSet.noneOf(SkinLayer.class));

    /** Order cosmetics are painted in: later entries are drawn over earlier ones (pants over a tucked-in shirt, etc). */
    public static final List<CosmeticSlot> RENDER_ORDER = List.of(SHIRT, PANTS, SHOES, HAT);

    private final String name;
    private final Set<SkinLayer> defaultHiddenLayers;

    CosmeticSlot(String name, Set<SkinLayer> defaultHiddenLayers) {
        this.name = name;
        this.defaultHiddenLayers = defaultHiddenLayers;
    }

    public String getName() {
        return name;
    }

    public int index() {
        return ordinal();
    }

    /** The player's own skin overlay layers that a cosmetic in this slot hides unless it says otherwise. */
    public EnumSet<SkinLayer> defaultHiddenLayers() {
        return defaultHiddenLayers.isEmpty() ? EnumSet.noneOf(SkinLayer.class) : EnumSet.copyOf(defaultHiddenLayers);
    }

    public boolean accepts(ItemStack stack) {
        return stack.getItem() instanceof CosmeticItem cosmetic && cosmetic.slot() == this;
    }

    public Component displayName() {
        return Component.translatable("cosmetic_slot.luckys_wardrobe." + name);
    }

    public static CosmeticSlot byIndex(int index) {
        return values()[index];
    }
}
