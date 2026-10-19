package net.luckyowlstudios.wardrobe;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.luckyowlstudios.wardrobe.network.OpenWardrobePayload;
import net.luckyowlstudios.wardrobe.network.SyncCosmeticsPayload;
import net.luckyowlstudios.wardrobe.network.WardrobeNetworking;
import net.luckyowlstudios.wardrobe.registry.ModItems;
import net.luckyowlstudios.wardrobe.registry.ModMenus;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public class LuckysWardrobe implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.init();

        ModItems.register((id, item) -> Registry.register(BuiltInRegistries.ITEM, id, item));
        ModMenus.register((id, menu) -> Registry.register(BuiltInRegistries.MENU, id, menu));

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(entries -> {
            for (Item item : ModItems.ALL) {
                entries.accept(item);
            }
        });

        PayloadTypeRegistry.playS2C().register(SyncCosmeticsPayload.TYPE, SyncCosmeticsPayload.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(OpenWardrobePayload.TYPE, OpenWardrobePayload.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(OpenWardrobePayload.TYPE,
                (payload, context) -> context.server().execute(() -> WardrobeNetworking.openWardrobe(context.player())));
    }
}
