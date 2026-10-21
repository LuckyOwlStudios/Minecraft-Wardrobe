package net.luckyowlstudios.wardrobe;

import net.luckyowlstudios.wardrobe.client.WardrobeClient;
import net.luckyowlstudios.wardrobe.datagen.WardrobeDataGenerators;
import net.luckyowlstudios.wardrobe.network.OpenWardrobePayload;
import net.luckyowlstudios.wardrobe.network.SyncCosmeticsPayload;
import net.luckyowlstudios.wardrobe.network.WardrobeNetworking;
import net.luckyowlstudios.wardrobe.registry.ModCreativeTabs;
import net.luckyowlstudios.wardrobe.registry.ModItems;
import net.luckyowlstudios.wardrobe.registry.ModMenus;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Constants.MOD_ID)
public class LuckysWardrobe {

    public LuckysWardrobe(IEventBus modBus) {
        CommonClass.init();

        modBus.addListener(LuckysWardrobe::onRegister);
        modBus.addListener(LuckysWardrobe::onRegisterPayloads);
        modBus.addListener(WardrobeDataGenerators::gatherData);
        if (FMLEnvironment.dist.isClient()) {
            LuckysWardrobeClient.init(modBus);
        }
    }

    private static void onRegister(RegisterEvent event) {
        event.register(Registries.ITEM, helper -> ModItems.register(helper::register));
        event.register(Registries.MENU, helper -> ModMenus.register(helper::register));
        event.register(Registries.CREATIVE_MODE_TAB, helper -> ModCreativeTabs.register(helper::register));
    }

    private static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(SyncCosmeticsPayload.TYPE, SyncCosmeticsPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> WardrobeClient.handleSync(payload)));
        registrar.playToServer(OpenWardrobePayload.TYPE, OpenWardrobePayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> WardrobeNetworking.openWardrobe((ServerPlayer) context.player())));
    }
}
