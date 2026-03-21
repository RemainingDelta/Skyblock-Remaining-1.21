package com.remainingdelta;

import com.remainingdelta.command.ConfigCommand;
import com.remainingdelta.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;

/**
 * Main client-side entry point for the SkyblockRemaining mod.
 * Handles the initialization of HUD overlays and client-specific event listeners.
 */
public class SkyblockRemainingClient implements ClientModInitializer {

  /**
   * Initializes the client mod.
   * Registers the mod config, the /sbr command, and a HUD render callback
   * to display truncated player coordinates (X, Y, Z) on the screen
   * with a semi-transparent background.
   */
  @Override
  public void onInitializeClient() {
    AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

    ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
      ConfigCommand.register(dispatcher);
    });

    HudRenderCallback.EVENT.register((guiGraphics, renderTickCounter) -> {
      Minecraft client = Minecraft.getInstance();
      if (client.player != null && !client.options.hideGui) {
        int x = (int) client.player.getX();
        int y = (int) client.player.getY();
        int z = (int) client.player.getZ();
        String coords = String.format("X: %d | Y: %d | Z: %d", x, y, z);
        guiGraphics.fill(5, 5, 125, 20, 0x90000000);
        guiGraphics.drawString(
            client.font,
            coords,
            10, 8,
            0xFFFFFFFF,
            true
        );
      }
    });
  }
}