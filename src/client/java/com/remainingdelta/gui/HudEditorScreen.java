package com.remainingdelta.gui;

import com.remainingdelta.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * A full-screen HUD position editor for SkyblockRemaining.
 * Dims the game world and renders all enabled HUD elements as draggable widgets.
 * Positions are saved to config on mouse release.
 */
public class HudEditorScreen extends Screen {

  private static final int HUD_W           = 122;
  private static final int HUD_H           = 14;
  private static final int HIGHLIGHT       = 0xFF5B8DD9;
  private static final int HIGHLIGHT_BG    = 0x335B8DD9;
  private static final int DISABLED_BG     = 0x33555555;
  private static final int DISABLED_BORDER = 0xFF555555;

  private boolean dragging = false;
  private int dragOffsetX, dragOffsetY;

  /**
   * Creates a new HudEditorScreen.
   */
  public HudEditorScreen() {
    super(Component.literal("HUD Editor"));
  }

  /**
   * Renders the dimmed game world overlay, all HUD elements,
   * and a help tooltip at the bottom of the screen.
   * Tracks mouse button state via GLFW to handle drag interactions.
   * Saves the config when the mouse button is released after dragging.
   *
   * @param g      the graphics context
   * @param mouseX the current mouse X position
   * @param mouseY the current mouse Y position
   * @param delta  the render tick delta
   */
  @Override
  public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
    boolean mouseDown = org.lwjgl.glfw.GLFW.glfwGetMouseButton(
        Minecraft.getInstance().getWindow().handle(),
        org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT
    ) == org.lwjgl.glfw.GLFW.GLFW_PRESS;

    if (!mouseDown && dragging) {
      dragging = false;
      ConfigManager.save();
    }

    if (mouseDown) {
      int hudX = ConfigManager.get().hudX;
      int hudY = ConfigManager.get().hudY;
      boolean enabled = ConfigManager.get().hudEnabled;

      if (!dragging && enabled
          && mouseX >= hudX - 3 && mouseX <= hudX + HUD_W + 3
          && mouseY >= hudY - 3 && mouseY <= hudY + HUD_H + 3) {
        dragging = true;
        dragOffsetX = mouseX - hudX;
        dragOffsetY = mouseY - hudY;
      }
    }

    if (dragging) {
      ConfigManager.get().hudX = mouseX - dragOffsetX;
      ConfigManager.get().hudY = mouseY - dragOffsetY;
    }

    g.fill(0, 0, this.width, this.height, 0x88000000);

    int hudX = ConfigManager.get().hudX;
    int hudY = ConfigManager.get().hudY;
    boolean enabled = ConfigManager.get().hudEnabled;

    boolean hovered = enabled
        && mouseX >= hudX - 3 && mouseX <= hudX + HUD_W + 3
        && mouseY >= hudY - 3 && mouseY <= hudY + HUD_H + 3;

    int borderColor = enabled ? HIGHLIGHT : DISABLED_BORDER;
    int bgColor     = enabled ? HIGHLIGHT_BG : DISABLED_BG;

    g.fill(hudX - 3, hudY - 3, hudX + HUD_W + 3, hudY + HUD_H + 3, bgColor);
    g.fill(hudX - 3, hudY - 3, hudX + HUD_W + 3, hudY - 2, borderColor);
    g.fill(hudX - 3, hudY + HUD_H + 2, hudX + HUD_W + 3, hudY + HUD_H + 3, borderColor);
    g.fill(hudX - 3, hudY - 3, hudX - 2, hudY + HUD_H + 3, borderColor);
    g.fill(hudX + HUD_W + 2, hudY - 3, hudX + HUD_W + 3, hudY + HUD_H + 3, borderColor);

    g.fill(hudX - 2, hudY - 2, hudX + HUD_W, hudY + HUD_H, 0x90000000);
    g.drawString(font, "X: 0 | Y: 64 | Z: 0", hudX, hudY,
        enabled ? 0xFFFFFFFF : 0xFF777777, true);

    if (hovered || dragging) {
      g.drawString(font, "Coordinates HUD", hudX, hudY - 14, HIGHLIGHT, false);
    } else if (!enabled) {
      g.drawString(font, "Coordinates HUD (disabled)", hudX, hudY - 14, DISABLED_BORDER, false);
    }

    String help = "Click and drag to reposition  -  Press Esc to save and exit";
    int helpW = font.width(help);
    g.fill(this.width / 2 - helpW / 2 - 4, this.height - 18,
        this.width / 2 + helpW / 2 + 4, this.height - 6, 0xAA000000);
    g.drawString(font, help, this.width / 2 - helpW / 2, this.height - 14, 0xFFAAAAAA, false);

    super.render(g, mouseX, mouseY, delta);
  }

  /**
   * Returns false to keep the game world rendering behind the editor.
   */
  @Override
  public boolean isPauseScreen() {
    return false;
  }
}
