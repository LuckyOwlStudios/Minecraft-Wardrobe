package net.luckyowlstudios.wardrobe.cosmetic;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * The cosmetic slots stored on every player, indexed by {@link CosmeticSlot#index()}. Saved with the player and kept
 * through death. Any change marks it dirty and the server sends it to the player and everyone tracking them on the
 * next tick.
 */
public class CosmeticInventory extends SimpleContainer {

    private boolean dirty = true;

    public CosmeticInventory() {
        super(CosmeticSlot.values().length);
    }

    public ItemStack get(CosmeticSlot slot) {
        return getItem(slot.index());
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return CosmeticSlot.byIndex(index).accepts(stack);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        dirty = true;
    }

    public void markDirty() {
        dirty = true;
    }

    /** Returns whether anything changed since the last call, and resets the flag. */
    public boolean consumeDirty() {
        boolean wasDirty = dirty;
        dirty = false;
        return wasDirty;
    }

    public List<ItemStack> snapshot() {
        List<ItemStack> copy = new ArrayList<>(getContainerSize());
        for (ItemStack stack : getItems()) {
            copy.add(stack.copy());
        }
        return copy;
    }

    /** Used client side when a sync arrives. */
    public void replaceAll(List<ItemStack> stacks) {
        for (int i = 0; i < getContainerSize(); i++) {
            setItem(i, i < stacks.size() ? stacks.get(i) : ItemStack.EMPTY);
        }
    }

    public void copyFrom(CosmeticInventory other) {
        replaceAll(other.snapshot());
    }

    /** The union of skin overlay layers hidden by everything currently worn. */
    public EnumSet<SkinLayer> hiddenSkinLayers() {
        EnumSet<SkinLayer> hidden = EnumSet.noneOf(SkinLayer.class);
        for (ItemStack stack : getItems()) {
            if (stack.getItem() instanceof CosmeticItem cosmetic) {
                hidden.addAll(cosmetic.cosmetic().hiddenSkinLayers());
            }
        }
        return hidden;
    }

    public CompoundTag save(HolderLookup.Provider registries) {
        return ContainerHelper.saveAllItems(new CompoundTag(), getItems(), registries);
    }

    public void load(CompoundTag tag, HolderLookup.Provider registries) {
        getItems().replaceAll(stack -> ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, getItems(), registries);
        setChanged();
    }
}
