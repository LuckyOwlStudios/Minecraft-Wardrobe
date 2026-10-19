package net.luckyowlstudios.wardrobe.datagen;

import net.luckyowlstudios.wardrobe.Constants;
import net.luckyowlstudios.wardrobe.cosmetic.CosmeticSlot;
import net.luckyowlstudios.wardrobe.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(PackOutput output) {
        super(output, Constants.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(ModItems.HAT, "Hat");
        add(ModItems.WHITE_HAT, "White Hat");
        add(ModItems.WHITE_SHIRT, "White Shirt");
        add(ModItems.WHITE_PANTS, "White Pants");
        add(ModItems.WHITE_SHOES, "White Shoes");

        add(slotKey(CosmeticSlot.HAT), "Hat");
        add(slotKey(CosmeticSlot.SHIRT), "Shirt");
        add(slotKey(CosmeticSlot.PANTS), "Pants");
        add(slotKey(CosmeticSlot.SHOES), "Shoes");

        add("container.luckys_wardrobe.wardrobe", "Wardrobe");
        add("itemGroup.luckys_wardrobe.cosmetics", "Cosmetics");
        add("key.categories.luckys_wardrobe", "Lucky's Wardrobe");
        add("key.luckys_wardrobe.open_wardrobe", "Open Wardrobe");
    }

    private static String slotKey(CosmeticSlot slot) {
        return "cosmetic_slot.luckys_wardrobe." + slot.getName();
    }
}
