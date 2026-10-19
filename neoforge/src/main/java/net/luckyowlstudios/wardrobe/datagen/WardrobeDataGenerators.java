package net.luckyowlstudios.wardrobe.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/**
 * Datagen for both loaders. Run {@code ./gradlew :neoforge:runData}. The output goes to
 * {@code common/src/generated/resources}, which the Fabric and NeoForge jars both include.
 */
public final class WardrobeDataGenerators {

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFiles = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), new ModLanguageProvider(output));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, existingFiles));
        generator.addProvider(event.includeServer(), new ModItemTagsProvider(output, event.getLookupProvider(), existingFiles));
    }

    private WardrobeDataGenerators() {
    }
}
