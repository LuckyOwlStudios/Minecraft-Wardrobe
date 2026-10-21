package net.luckyowlstudios.wardrobe.datagen;

import net.luckyowlstudios.wardrobe.Constants;
import net.luckyowlstudios.wardrobe.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()), Constants.MOD_ID,
                existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Dyeable in a crafting grid, like leather armor. The hat's band picks up the color.
        tag(ItemTags.DYEABLE).add(ModItems.HAT);
    }
}
