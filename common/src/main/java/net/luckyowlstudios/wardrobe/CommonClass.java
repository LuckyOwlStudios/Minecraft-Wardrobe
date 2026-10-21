package net.luckyowlstudios.wardrobe;

import net.luckyowlstudios.wardrobe.platform.Services;

// Shared setup that runs on every loader. Registration itself is driven by each loader's entrypoint, which hands its
// registration callback to ModItems / ModMenus, because Fabric and NeoForge register content at different times.
public class CommonClass {

    public static void init() {
        Constants.LOG.info("Loading {} on {}", Constants.MOD_NAME, Services.PLATFORM.getPlatformName());
    }
}
