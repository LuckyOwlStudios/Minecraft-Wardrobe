package net.luckyowlstudios.wardrobe;

import net.luckyowlstudios.wardrobe.client.WardrobeClient;
import net.luckyowlstudios.wardrobe.client.WardrobeScreen;
import net.luckyowlstudios.wardrobe.registry.ModMenus;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;

/** Client-only setup. Only loaded when running on the client, so it can touch client classes. */
public final class LuckysWardrobeClient {

    static void init(IEventBus modBus) {
        WardrobeClient.init();
        modBus.addListener(LuckysWardrobeClient::onRegisterKeyMappings);
        modBus.addListener(LuckysWardrobeClient::onRegisterScreens);
        NeoForge.EVENT_BUS.addListener(LuckysWardrobeClient::onClientTick);
    }

    private static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(WardrobeClient.OPEN_WARDROBE);
    }

    private static void onRegisterScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.WARDROBE, WardrobeScreen::new);
    }

    private static void onClientTick(ClientTickEvent.Post event) {
        WardrobeClient.onClientTick(Minecraft.getInstance());
    }

    private LuckysWardrobeClient() {
    }
}
