package com.remainingdelta.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "skyblockremaining")
public class ModConfig implements ConfigData {

  @Comment("Whether the HUD is enabled")
  public boolean hudEnabled = true;
}