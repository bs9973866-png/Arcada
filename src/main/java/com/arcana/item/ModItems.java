package com.arcana.item;

import com.arcana.Arcana;
import com.arcana.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

import java.util.function.Function;

public class ModItems {
    public static final Item ARCANE_CRYSTAL = register("arcane_crystal", Item::new, new Item.Settings());
    public static final Item MANA_CRYSTAL = register("mana_crystal", ManaCrystalItem::new, new Item.Settings().maxCount(16));
    public static final Item ARCANE_STAFF = register("arcane_staff", StaffItem::new, new Item.Settings().maxCount(1));
    public static final Item MANA_ALTAR = register("mana_altar",
            s -> new BlockItem(ModBlocks.MANA_ALTAR, s),
            new Item.Settings().useBlockPrefixedTranslationKey());

    public static final ItemGroup GROUP = Registry.register(Registries.ITEM_GROUP, Arcana.id("main"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ARCANE_STAFF))
                    .displayName(Text.translatable("itemGroup.arcana"))
                    .entries((ctx, entries) -> {
                        entries.add(ARCANE_CRYSTAL);
                        entries.add(MANA_CRYSTAL);
                        entries.add(ARCANE_STAFF);
                        entries.add(MANA_ALTAR);
                    })
                    .build());

    private static Item register(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Arcana.id(name));
        return Registry.register(Registries.ITEM, key, factory.apply(settings.registryKey(key)));
    }

    public static void init() {
    }
}
