package net.luckyowlstudios.wardrobe.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.luckyowlstudios.wardrobe.cosmetic.CosmeticHolder;
import net.luckyowlstudios.wardrobe.network.OpenWardrobePayload;
import net.luckyowlstudios.wardrobe.network.SyncCosmeticsPayload;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

/** Client-side behaviour shared by both loaders. Each loader registers the key mapping and calls in here. */
public final class WardrobeClient {

    public static final KeyMapping OPEN_WARDROBE = new KeyMapping("key.luckys_wardrobe.open_wardrobe",
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, "key.categories.luckys_wardrobe");

    public static void onClientTick(Minecraft minecraft) {
        while (OPEN_WARDROBE.consumeClick()) {
            if (minecraft.player != null && minecraft.screen == null && minecraft.getConnection() != null) {
                minecraft.getConnection().send(new ServerboundCustomPayloadPacket(OpenWardrobePayload.INSTANCE));
            }
        }
    }

    public static void handleSync(SyncCosmeticsPayload payload) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }
        Entity entity = minecraft.player != null && minecraft.player.getId() == payload.entityId()
                ? minecraft.player
                : minecraft.level.getEntity(payload.entityId());
        if (entity instanceof Player player) {
            CosmeticHolder.get(player).replaceAll(payload.items());
        }
    }

    private WardrobeClient() {
    }
}
