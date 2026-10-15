package com.arcana.net;

import com.arcana.magic.ManaData;
import com.arcana.magic.ModAttachments;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class ModNetworking {
    public static void init() {
        PayloadTypeRegistry.playS2C().register(ManaSyncPayload.ID, ManaSyncPayload.CODEC);
    }

    public static void sync(ServerPlayerEntity p) {
        ManaData d = p.getAttachedOrCreate(ModAttachments.MANA);
        ServerPlayNetworking.send(p, new ManaSyncPayload(d.mana, d.maxMana(), d.level, d.spell));
    }
}
