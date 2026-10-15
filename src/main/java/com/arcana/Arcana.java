package com.arcana;

import com.arcana.block.ModBlocks;
import com.arcana.item.ModItems;
import com.arcana.magic.ArcanaCommands;
import com.arcana.magic.ManaEvents;
import com.arcana.magic.ModAttachments;
import com.arcana.net.ModNetworking;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Arcana implements ModInitializer {
    public static final String MOD_ID = "arcana";

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModAttachments.init();
        ModBlocks.init();
        ModItems.init();
        ModNetworking.init();
        ManaEvents.init();
        ArcanaCommands.init();
    }
}
