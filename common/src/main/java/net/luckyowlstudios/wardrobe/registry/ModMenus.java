package net.luckyowlstudios.wardrobe.registry;

import net.luckyowlstudios.wardrobe.Constants;
import net.luckyowlstudios.wardrobe.menu.WardrobeMenu;
import net.luckyowlstudios.wardrobe.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;

import java.util.function.BiConsumer;

public final class ModMenus {

    public static MenuType<WardrobeMenu> WARDROBE;

    public static void register(BiConsumer<ResourceLocation, MenuType<?>> registrar) {
        WARDROBE = Services.PLATFORM.createMenuType(WardrobeMenu::new);
        registrar.accept(Constants.id("wardrobe"), WARDROBE);
    }

    private ModMenus() {
    }
}
