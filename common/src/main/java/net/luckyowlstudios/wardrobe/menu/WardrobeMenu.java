package net.luckyowlstudios.wardrobe.menu;

import com.mojang.datafixers.util.Pair;
import net.luckyowlstudios.wardrobe.cosmetic.CosmeticHolder;
import net.luckyowlstudios.wardrobe.cosmetic.CosmeticInventory;
import net.luckyowlstudios.wardrobe.cosmetic.CosmeticItem;
import net.luckyowlstudios.wardrobe.cosmetic.CosmeticSlot;
import net.luckyowlstudios.wardrobe.registry.ModMenus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/** The four cosmetic slots (laid out like the armor column) plus the player's inventory. */
public class WardrobeMenu extends AbstractContainerMenu {

    private static final int COSMETIC_END = CosmeticSlot.values().length;
    private static final int INVENTORY_END = COSMETIC_END + 27;
    private static final int HOTBAR_END = INVENTORY_END + 9;

    public WardrobeMenu(int containerId, Inventory playerInventory) {
        super(ModMenus.WARDROBE, containerId);
        CosmeticInventory cosmetics = CosmeticHolder.get(playerInventory.player);
        for (CosmeticSlot slot : CosmeticSlot.values()) {
            addSlot(new WardrobeSlot(cosmetics, slot, 8, 8 + slot.index() * 18));
        }
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInventory, column, 8 + column * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index < COSMETIC_END) {
            if (!moveItemStackTo(stack, COSMETIC_END, HOTBAR_END, true)) {
                return ItemStack.EMPTY;
            }
        } else if (stack.getItem() instanceof CosmeticItem cosmetic && !slots.get(cosmetic.slot().index()).hasItem()) {
            int target = cosmetic.slot().index();
            if (!moveItemStackTo(stack, target, target + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < INVENTORY_END) {
            if (!moveItemStackTo(stack, INVENTORY_END, HOTBAR_END, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, COSMETIC_END, INVENTORY_END, false)) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, stack);
        return original;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    private static class WardrobeSlot extends Slot {
        private final CosmeticSlot cosmeticSlot;

        WardrobeSlot(Container container, CosmeticSlot cosmeticSlot, int x, int y) {
            super(container, cosmeticSlot.index(), x, y);
            this.cosmeticSlot = cosmeticSlot;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return cosmeticSlot.accepts(stack);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
            ResourceLocation icon = switch (cosmeticSlot) {
                case HAT -> InventoryMenu.EMPTY_ARMOR_SLOT_HELMET;
                case SHIRT -> InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE;
                case PANTS -> InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS;
                case SHOES -> InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS;
            };
            return Pair.of(InventoryMenu.BLOCK_ATLAS, icon);
        }
    }
}
