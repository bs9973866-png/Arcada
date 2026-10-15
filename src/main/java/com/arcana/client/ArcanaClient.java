package com.arcana.client;

import com.arcana.Arcana;
import com.arcana.magic.Spell;
import com.arcana.net.ManaSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

public class ArcanaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(ManaSyncPayload.ID, (payload, context) -> {
            ClientManaState.mana = payload.mana();
            ClientManaState.maxMana = payload.maxMana();
            ClientManaState.level = payload.level();
            ClientManaState.spell = payload.spell();
        });

        HudElementRegistry.attachElementAfter(VanillaHudElements.HOTBAR, Arcana.id("mana_bar"),
                (DrawContext ctx, RenderTickCounter tickCounter) -> {
                    MinecraftClient mc = MinecraftClient.getInstance();
                    float ratio = ClientManaState.maxMana <= 0 ? 0f
                            : Math.min(1f, (float) ClientManaState.mana / ClientManaState.maxMana);
                    int x = 10, y = 10, w = 100;
                    ctx.fill(x - 1, y - 1, x + w + 1, y + 7, 0xFF000000);
                    ctx.fill(x, y, x + (int) (w * ratio), y + 6, 0xFF3A6BFF);
                    Text line = Text.translatable("hud.arcana.mana", ClientManaState.mana, ClientManaState.maxMana,
                            ClientManaState.level);
                    ctx.drawText(mc.textRenderer, line, x, y + 10, 0xFFFFFFFF, true);
                    ctx.drawText(mc.textRenderer, Spell.byIndex(ClientManaState.spell).displayName(),
                            x, y + 20, 0xFFD9B3FF, true);
                });
    }
}
