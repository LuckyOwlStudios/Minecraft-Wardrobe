package net.luckyowlstudios.wardrobe.network;

import net.luckyowlstudios.wardrobe.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** Server to client: the full contents of one player's cosmetic slots. */
public record SyncCosmeticsPayload(int entityId, List<ItemStack> items) implements CustomPacketPayload {

    public static final Type<SyncCosmeticsPayload> TYPE = new Type<>(Constants.id("sync_cosmetics"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncCosmeticsPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SyncCosmeticsPayload::entityId,
            ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list()), SyncCosmeticsPayload::items,
            SyncCosmeticsPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
