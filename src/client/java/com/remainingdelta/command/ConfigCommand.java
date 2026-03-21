package com.remainingdelta.command;

import com.mojang.brigadier.CommandDispatcher;
import com.remainingdelta.gui.ConfigScreen;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;

/**
 * Registers the /sbr client command for SkyblockRemaining.
 * Opens the mod's config screen when the command is executed.
 */
public class ConfigCommand {

  /**
   * Registers the /sbr command with the given command dispatcher.
   * The config screen is scheduled to open on the next client tick
   * to avoid conflicts with the chat screen closing.
   *
   * @param dispatcher the client command dispatcher to register with
   */
  public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
    dispatcher.register(
        ClientCommandManager.literal("sbr")
            .executes(context -> {
              Minecraft client = Minecraft.getInstance();
              client.schedule(() -> client.setScreen(new ConfigScreen()));
              return 1;
            })
    );
  }
}