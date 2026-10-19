package net.luckyowlstudios.wardrobe.datagen;

import net.luckyowlstudios.wardrobe.Constants;
import net.luckyowlstudios.wardrobe.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    /** Every item gets a flat model using {@code textures/item/<item>.png}. */
    @Override
    protected void registerModels() {
        for (Item item : ModItems.ALL) {
            basicItem(item);
        }
    }
}
