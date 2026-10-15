package com.arcana.block;

import com.arcana.magic.ManaData;
import com.arcana.magic.ModAttachments;
import com.arcana.net.ModNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ManaAltarBlock extends Block {
    public ManaAltarBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient()) return ActionResult.SUCCESS;
        ManaData d = player.getAttachedOrCreate(ModAttachments.MANA);
        d.mana = d.maxMana();
        if (player instanceof ServerPlayerEntity sp) {
            ModNetworking.sync(sp);
            sp.sendMessage(Text.translatable("message.arcana.mana_restored"), true);
        }
        return ActionResult.SUCCESS;
    }
}
