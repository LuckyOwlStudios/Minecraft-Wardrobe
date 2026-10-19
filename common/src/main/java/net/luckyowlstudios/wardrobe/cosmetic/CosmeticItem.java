package net.luckyowlstudios.wardrobe.cosmetic;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

/** An item that can be worn in one of the player's cosmetic slots. Gives no protection, only looks. */
public class CosmeticItem extends Item {

    private final CosmeticSlot slot;
    private final CosmeticProperties cosmetic;

    public CosmeticItem(CosmeticSlot slot, CosmeticProperties cosmetic, Properties properties) {
        super(properties);
        this.slot = slot;
        this.cosmetic = cosmetic;
    }

    public CosmeticSlot slot() {
        return slot;
    }

    public CosmeticProperties cosmetic() {
        return cosmetic;
    }

    /** Right-click to put it on, swapping with whatever is in that cosmetic slot (same flow as vanilla armor). */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        CosmeticInventory cosmetics = CosmeticHolder.get(player);
        ItemStack worn = cosmetics.get(slot);
        if (ItemStack.matches(held, worn)) {
            return InteractionResultHolder.fail(held);
        }
        if (!level.isClientSide()) {
            player.awardStat(Stats.ITEM_USED.get(this));
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARMOR_EQUIP_LEATHER.value(),
                    SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        ItemStack returned = worn.isEmpty() ? held : worn.copyAndClear();
        ItemStack toWear = player.isCreative() ? held.copy() : held.copyAndClear();
        cosmetics.setItem(slot.index(), toWear);
        return InteractionResultHolder.sidedSuccess(returned, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.luckys_wardrobe.cosmetic", slot.displayName()).withStyle(ChatFormatting.GRAY));
    }
}
