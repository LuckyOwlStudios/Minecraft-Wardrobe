package net.luckyowlstudios.wardrobe;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.luckyowlstudios.wardrobe.network.OpenWardrobePayload;
import net.luckyowlstudios.wardrobe.network.SyncCosmeticsPayload;
import net.luckyowlstudios.wardrobe.network.WardrobeNetworking;
import net.luckyowlstudios.wardrobe.registry.ModCreativeTabs;
import net.luckyowlstudios.wardrobe.registry.ModItems;
import net.luckyowlstudios.wardrobe.registry.ModMenus;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class LuckysWardrobe implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.init();

        ModItems.register((id, item) -> Registry.register(BuiltInRegistries.ITEM, id, item));
        ModMenus.register((id, menu) -> Registry.register(BuiltInRegistries.MENU, id, menu));
        ModCreativeTabs.register((id, tab) -> Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id, tab));

        PayloadTypeRegistry.playS2C().register(SyncCosmeticsPayload.TYPE, SyncCosmeticsPayload.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(OpenWardrobePayload.TYPE, OpenWardrobePayload.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(OpenWardrobePayload.TYPE,
                (payload, context) -> context.server().execute(() -> WardrobeNetworking.openWardrobe(context.player())));
    }
}
