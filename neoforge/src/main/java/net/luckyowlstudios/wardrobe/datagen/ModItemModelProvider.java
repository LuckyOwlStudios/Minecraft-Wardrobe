package net.luckyowlstudios.wardrobe.datagen;

import net.luckyowlstudios.wardrobe.Constants;
import net.luckyowlstudios.wardrobe.registry.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    /**
     * Every item gets a flat model using {@code textures/item/<item>.png}. Dyeable items add
     * {@code textures/item/<item>_dye_overlay.png} as layer1, which the item color tints.
     */
    @Override
    protected void registerModels() {
        for (Item item : ModItems.ALL) {
            if (item == ModItems.HAT) {
                dyeableItem(item);
            } else {
                basicItem(item);
            }
        }
    }

    private void dyeableItem(Item item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        getBuilder(id.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "item/" + id.getPath()))
                .texture("layer1", ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "item/" + id.getPath() + "_dye_overlay"));
    }
}
