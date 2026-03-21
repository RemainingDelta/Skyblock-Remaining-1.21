package com.remainingdelta;

import com.remainingdelta.gui.ConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * Integrates SkyblockRemaining with Mod Menu.
 * Provides a config screen factory so the mod appears in the Mod Menu list
 * with a clickable config button.
 */
public class ModMenuIntegration implements ModMenuApi {

  /**
   * Returns the factory used to create the mod's config screen.
   * Opens the custom ConfigScreen when the user clicks the config button in Mod Menu.
   */
  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    return parent -> new ConfigScreen();
  }
}