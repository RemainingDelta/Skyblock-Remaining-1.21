package com.remainingdelta;

import com.remainingdelta.command.ConfigCommand;
import com.remainingdelta.config.ConfigManager;
import com.remainingdelta.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;

/**
 * Main client-side entry point for the SkyblockRemaining mod.
 * Handles config loading, command registration, and HUD rendering.
 */
public class SkyblockRemainingClient implements ClientModInitializer {

  /**
   * Initializes the client mod.
   * Loads the mod config from disk, registers the /sbr command,
   * and registers a HUD render callback to display truncated player
   * coordinates (X, Y, Z) at the position and visibility defined by the config.
   */
  @Override
  public void onInitializeClient() {
    ConfigManager.load();

    ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
      ConfigCommand.register(dispatcher);
    });

    HudRenderCallback.EVENT.register((guiGraphics, renderTickCounter) -> {
      Minecraft client = Minecraft.getInstance();
      ModConfig config = ConfigManager.get();

      if (client.player != null && !client.options.hideGui && config.hudEnabled) {
        int x = (int) client.player.getX();
        int y = (int) client.player.getY();
        int z = (int) client.player.getZ();
        String coords = String.format("X: %d | Y: %d | Z: %d", x, y, z);
        guiGraphics.fill(config.hudX - 2, config.hudY - 2, config.hudX + 122, config.hudY
            + 12, 0x90000000);
        guiGraphics.drawString(
            client.font,
            coords,
            config.hudX, config.hudY,
            0xFFFFFFFF,
            true
        );
      }
    });
  }
}
