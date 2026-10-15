package com.arcana.magic;

import com.arcana.net.ModNetworking;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ManaEvents {
    private static int tick = 0;

    public static void init() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                ModNetworking.sync(handler.getPlayer()));

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (++tick < 20) return;
            tick = 0;
            for (ServerPlayerEntity p : server.getPlayerManager().getPlayerList()) {
                ManaData d = p.getAttachedOrCreate(ModAttachments.MANA);
                int max = d.maxMana();
                if (d.mana < max) {
                    d.mana = Math.min(max, d.mana + d.regenPerSecond());
                    ModNetworking.sync(p);
                }
            }
        });

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (source.getAttacker() instanceof ServerPlayerEntity p && !(entity instanceof PlayerEntity)) {
                giveXp(p, 5 + (int) (entity.getMaxHealth() / 4));
            }
        });
    }

    public static void giveXp(ServerPlayerEntity p, int amount) {
        ManaData d = p.getAttachedOrCreate(ModAttachments.MANA);
        if (d.addXp(amount)) {
            p.sendMessage(Text.translatable("message.arcana.level_up", d.level), false);
        }
        ModNetworking.sync(p);
    }
}
