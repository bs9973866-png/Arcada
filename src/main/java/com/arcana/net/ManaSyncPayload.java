package com.arcana.net;

import com.arcana.Arcana;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record ManaSyncPayload(int mana, int maxMana, int level, int spell) implements CustomPayload {
    public static final CustomPayload.Id<ManaSyncPayload> ID = new CustomPayload.Id<>(Arcana.id("mana_sync"));
    public static final PacketCodec<RegistryByteBuf, ManaSyncPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, ManaSyncPayload::mana,
            PacketCodecs.VAR_INT, ManaSyncPayload::maxMana,
            PacketCodecs.VAR_INT, ManaSyncPayload::level,
            PacketCodecs.VAR_INT, ManaSyncPayload::spell,
            ManaSyncPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
