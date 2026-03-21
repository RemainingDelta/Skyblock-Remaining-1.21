package com.remainingdelta.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

/**
 * Defines the configuration schema for SkyblockRemaining.
 * Fields in this class are automatically serialized to and from
 * a JSON file by AutoConfig. Add new config fields here as needed.
 */
@Config(name = "skyblockremaining")
public class ModConfig implements ConfigData {

  /**
   * Whether the HUD overlay is enabled.
   * Defaults to true.
   */
  @Comment("Whether the HUD is enabled")
  public boolean hudEnabled = true;

  /**
   * The X position of the HUD overlay on screen.
   * Defaults to 5.
   */
  @Comment("HUD X position")
  public int hudX = 5;

  /**
   * The Y position of the HUD overlay on screen.
   * Defaults to 5.
   */
  @Comment("HUD Y position")
  public int hudY = 5;
}
