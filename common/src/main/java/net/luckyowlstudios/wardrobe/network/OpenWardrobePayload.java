package net.luckyowlstudios.wardrobe.network;

import net.luckyowlstudios.wardrobe.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/** Client to server: the wardrobe key was pressed. */
public record OpenWardrobePayload() implements CustomPacketPayload {

    public static final OpenWardrobePayload INSTANCE = new OpenWardrobePayload();
    public static final Type<OpenWardrobePayload> TYPE = new Type<>(Constants.id("open_wardrobe"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenWardrobePayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
