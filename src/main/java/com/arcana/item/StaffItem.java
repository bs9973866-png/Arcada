package com.arcana.item;

import com.arcana.magic.ManaData;
import com.arcana.magic.ModAttachments;
import com.arcana.magic.Spell;
import com.arcana.net.ModNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class StaffItem extends Item {
    public StaffItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) return ActionResult.SUCCESS;
        ServerPlayerEntity p = (ServerPlayerEntity) user;
        ServerWorld sw = (ServerWorld) world;
        ManaData d = p.getAttachedOrCreate(ModAttachments.MANA);

        if (p.isSneaking()) {
            d.cycleSpell();
            ModNetworking.sync(p);
            p.sendMessage(Text.translatable("message.arcana.spell_selected", d.currentSpell().displayName()), true);
            return ActionResult.SUCCESS;
        }

        Spell s = d.currentSpell();
        if (d.level < s.minLevel) {
            p.sendMessage(Text.translatable("message.arcana.low_level", s.minLevel), true);
            return ActionResult.FAIL;
        }
        if (d.mana < s.cost) {
            p.sendMessage(Text.translatable("message.arcana.no_mana"), true);
            return ActionResult.FAIL;
        }
        if (s.cast(sw, p, d.level)) {
            d.mana -= s.cost;
            p.getItemCooldownManager().set(p.getStackInHand(hand), 10);
            ModNetworking.sync(p);
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }
}
