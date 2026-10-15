package com.arcana.block;

import com.arcana.Arcana;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;

import java.util.function.Function;

public class ModBlocks {
    public static final Block MANA_ALTAR = register("mana_altar",
            ManaAltarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PURPLE)
                    .strength(3.0f, 6.0f)
                    .luminance(state -> 8)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK));

    private static Block register(String name, Function<AbstractBlock.Settings, Block> factory,
                                  AbstractBlock.Settings settings) {
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, Arcana.id(name));
        return Registry.register(Registries.BLOCK, key, factory.apply(settings.registryKey(key)));
    }

    public static void init() {
    }
}
