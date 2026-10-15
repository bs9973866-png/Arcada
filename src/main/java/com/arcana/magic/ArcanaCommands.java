package com.arcana.magic;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ArcanaCommands {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(CommandManager.literal("arcana")
                        .then(CommandManager.literal("info").executes(ctx -> {
                            ServerPlayerEntity p = ctx.getSource().getPlayerOrThrow();
                            ManaData d = p.getAttachedOrCreate(ModAttachments.MANA);
                            ctx.getSource().sendFeedback(() -> Text.translatable("message.arcana.info",
                                    d.level, d.mana, d.maxMana(), d.xp, d.xpToNext(), d.currentSpell().displayName()), false);
                            return 1;
                        }))
                        .then(CommandManager.literal("spells").executes(ctx -> {
                            for (Spell s : Spell.values()) {
                                ctx.getSource().sendFeedback(() -> Text.translatable("message.arcana.spell_line",
                                        s.displayName(), s.cost, s.minLevel), false);
                            }
                            return 1;
                        }))));
    }
}
