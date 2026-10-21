package net.luckyowlstudios.wardrobe.registry;

import net.luckyowlstudios.wardrobe.Constants;
import net.luckyowlstudios.wardrobe.cosmetic.CosmeticItem;
import net.luckyowlstudios.wardrobe.cosmetic.CosmeticProperties;
import net.luckyowlstudios.wardrobe.cosmetic.CosmeticSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public final class ModItems {

    /** Every item this mod adds, in creative tab order. */
    public static final List<Item> ALL = new ArrayList<>();

    public static Item HAT;
    public static Item WHITE_HAT;
    public static Item WHITE_SHIRT;
    public static Item WHITE_PANTS;
    public static Item WHITE_SHOES;

    /** Called by each loader with its own way of registering an item. */
    public static void register(BiConsumer<ResourceLocation, Item> registrar) {
        HAT = cosmetic(registrar, "hat", CosmeticSlot.HAT,
                CosmeticProperties.builder(CosmeticSlot.HAT).noSkinTexture().hideHeadOverlay().extraModel(Constants.id("hat"))
                        .liftOverHelmet(2.0F));
        WHITE_HAT = cosmetic(registrar, "white_hat", CosmeticSlot.HAT,
                CosmeticProperties.builder(CosmeticSlot.HAT).hideHeadOverlay());
        WHITE_SHIRT = cosmetic(registrar, "white_shirt", CosmeticSlot.SHIRT,
                CosmeticProperties.builder(CosmeticSlot.SHIRT));
        WHITE_PANTS = cosmetic(registrar, "white_pants", CosmeticSlot.PANTS,
                CosmeticProperties.builder(CosmeticSlot.PANTS));
        WHITE_SHOES = cosmetic(registrar, "white_shoes", CosmeticSlot.SHOES,
                CosmeticProperties.builder(CosmeticSlot.SHOES));
    }

    private static Item cosmetic(BiConsumer<ResourceLocation, Item> registrar, String name, CosmeticSlot slot,
                                 CosmeticProperties.Builder cosmetic) {
        Item item = new CosmeticItem(slot, cosmetic.build(), new Item.Properties().stacksTo(1));
        registrar.accept(Constants.id(name), item);
        ALL.add(item);
        return item;
    }

    private ModItems() {
    }
}
