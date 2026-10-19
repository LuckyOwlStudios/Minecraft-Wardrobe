package net.luckyowlstudios.wardrobe.registry;

import net.luckyowlstudios.wardrobe.Constants;
import net.luckyowlstudios.wardrobe.platform.Services;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;

public final class ModCreativeTabs {

    public static CreativeModeTab COSMETICS;

    /** Called by each loader with its own way of registering a creative tab. Register after {@link ModItems}. */
    public static void register(BiConsumer<ResourceLocation, CreativeModeTab> registrar) {
        COSMETICS = Services.PLATFORM.creativeTabBuilder()
                .title(Component.translatable("itemGroup.luckys_wardrobe.cosmetics"))
                .icon(() -> new ItemStack(ModItems.HAT))
                .displayItems((parameters, output) -> {
                    for (Item item : ModItems.ALL) {
                        output.accept(item);
                    }
                })
                .build();
        registrar.accept(Constants.id("cosmetics"), COSMETICS);
    }

    private ModCreativeTabs() {
    }
}
