package net.luckyowlstudios.wardrobe.network;

import net.luckyowlstudios.wardrobe.cosmetic.CosmeticHolder;
import net.luckyowlstudios.wardrobe.menu.WardrobeMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;

/**
 * Server side of the mod's networking. Payload types are registered per loader; sending goes through vanilla's
 * custom payload packet, which both Fabric and NeoForge route to registered payloads.
 */
public final class WardrobeNetworking {

    private static final Component WARDROBE_TITLE = Component.translatable("container.luckys_wardrobe.wardrobe");

    public static void openWardrobe(ServerPlayer player) {
        player.openMenu(new SimpleMenuProvider((containerId, inventory, p) -> new WardrobeMenu(containerId, inventory), WARDROBE_TITLE));
    }

    public static SyncCosmeticsPayload syncPayload(Player player) {
        return new SyncCosmeticsPayload(player.getId(), CosmeticHolder.get(player).snapshot());
    }

    /** Sends a player's cosmetics to themselves and everyone currently tracking them. */
    public static void broadcast(ServerPlayer player) {
        player.serverLevel().getChunkSource().broadcastAndSend(player, new ClientboundCustomPayloadPacket(syncPayload(player)));
    }

    /** Sends {@code target}'s cosmetics to one player, e.g. when they start seeing them. */
    public static void sendTo(ServerPlayer receiver, Player target) {
        receiver.connection.send(new ClientboundCustomPayloadPacket(syncPayload(target)));
    }

    private WardrobeNetworking() {
    }
}
