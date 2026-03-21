package com.remainingdelta.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Manages loading and saving of the mod configuration.
 * Serializes {@link ModConfig} to and from
 * {@code config/SkyblockRemaining/general.json} using Gson.
 */
public class ConfigManager {

  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private static final Path CONFIG_PATH = FabricLoader.getInstance()
      .getConfigDir()
      .resolve("SkyblockRemaining")
      .resolve("general.json");

  private static ModConfig instance = new ModConfig();

  /**
   * Returns the current in-memory config instance.
   *
   * @return the active {@link ModConfig}
   */
  public static ModConfig get() {
    return instance;
  }

  /**
   * Loads the config from disk. If the file does not exist,
   * a default config is used and saved to disk.
   */
  public static void load() {
    try {
      if (Files.exists(CONFIG_PATH)) {
        String json = Files.readString(CONFIG_PATH);
        instance = GSON.fromJson(json, ModConfig.class);
      } else {
        instance = new ModConfig();
        save();
      }
    } catch (IOException e) {
      System.err.println("[SkyblockRemaining] Failed to load config: " + e.getMessage());
      instance = new ModConfig();
    }
  }

  /**
   * Saves the current in-memory config to disk.
   * Creates the config directory if it does not exist.
   */
  public static void save() {
    try {
      Files.createDirectories(CONFIG_PATH.getParent());
      Files.writeString(CONFIG_PATH, GSON.toJson(instance));
    } catch (IOException e) {
      System.err.println("[SkyblockRemaining] Failed to save config: " + e.getMessage());
    }
  }
}
