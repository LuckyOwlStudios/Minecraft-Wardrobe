package net.luckyowlstudios.wardrobe;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.luckyowlstudios.wardrobe.client.WardrobeClient;
import net.luckyowlstudios.wardrobe.client.WardrobeScreen;
import net.luckyowlstudios.wardrobe.network.SyncCosmeticsPayload;
import net.luckyowlstudios.wardrobe.registry.ModMenus;
import net.minecraft.client.gui.screens.MenuScreens;

public class LuckysWardrobeClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        WardrobeClient.init();
        KeyBindingHelper.registerKeyBinding(WardrobeClient.OPEN_WARDROBE);
        ClientTickEvents.END_CLIENT_TICK.register(WardrobeClient::onClientTick);
        ClientPlayNetworking.registerGlobalReceiver(SyncCosmeticsPayload.TYPE,
                (payload, context) -> context.client().execute(() -> WardrobeClient.handleSync(payload)));
        MenuScreens.register(ModMenus.WARDROBE, WardrobeScreen::new);
    }
}
