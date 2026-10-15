package com.arcana.item;

import com.arcana.magic.ManaData;
import com.arcana.magic.ModAttachments;
import com.arcana.net.ModNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ManaCrystalItem extends Item {
    public ManaCrystalItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) return ActionResult.SUCCESS;
        ManaData d = user.getAttachedOrCreate(ModAttachments.MANA);
        if (d.mana >= d.maxMana()) return ActionResult.FAIL;
        d.mana = Math.min(d.maxMana(), d.mana + 50);
        ItemStack stack = user.getStackInHand(hand);
        stack.decrementUnlessCreative(1, user);
        if (user instanceof ServerPlayerEntity sp) {
            ModNetworking.sync(sp);
            sp.sendMessage(Text.translatable("message.arcana.mana_restored"), true);
        }
        return ActionResult.SUCCESS;
    }
}
