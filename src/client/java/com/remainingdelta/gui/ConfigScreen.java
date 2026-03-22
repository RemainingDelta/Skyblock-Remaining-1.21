package com.remainingdelta.gui;

import com.remainingdelta.config.ConfigManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * The main configuration screen for SkyblockRemaining.
 * Renders a custom dark-themed UI with a category panel on the left
 * and a content panel on the right. Categories are navigable via
 * invisible button widgets overlaid on the rendered category list.
 */
public class ConfigScreen extends Screen {

  private static final int SCREEN_WIDTH  = 500;
  private static final int SCREEN_HEIGHT = 380;
  private static final int PANEL_WIDTH   = 150;
  private static final int HEADER_HEIGHT = 20;
  private static final int INNER_PADDING = 6;

  private static final int OUTER_BG    = 0xFF161616;
  private static final int HEADER_BG   = 0xFF111111;
  private static final int PANEL_BG    = 0xFF1A1A1A;
  private static final int CONTENT_BG  = 0xFF1E1E1E;
  private static final int BORDER      = 0xFF3A3A3A;
  private static final int INNER_BG    = 0xFF181818;
  private static final int SELECTED_BG = 0xFF2C2C2C;
  private static final int ACCENT      = 0xFF5B8DD9;
  private static final int TEXT        = 0xFFEEEEEE;
  private static final int SUBTEXT     = 0xFF777777;

  private final List<String> categories = List.of("About", "General");
  private int selectedCategory = 0;
  private int startX, startY;

  private final String modVersion = net.fabricmc.loader.api.FabricLoader.getInstance()
      .getModContainer("skyblockremaining")
      .map(c -> c.getMetadata().getVersion().getFriendlyString())
      .orElse("unknown");

  /**
   * Creates a new ConfigScreen with the title "SkyblockRemaining Config".
   */
  public ConfigScreen() {
    super(Component.literal("SkyblockRemaining Config"));
  }

  /**
   * Initializes the screen by calculating the centered position,
   * registering transparent click widgets over each category entry,
   * and registering the General page interactive widgets.
   */
  @Override
  protected void init() {
    startX = (this.width - SCREEN_WIDTH) / 2;
    startY = (this.height - SCREEN_HEIGHT) / 2 - 20;

    int lbX = startX + INNER_PADDING;
    int lbY = startY + HEADER_HEIGHT + 1 + INNER_PADDING;
    int lbW = PANEL_WIDTH - INNER_PADDING * 2;

    for (int i = 0; i < categories.size(); i++) {
      final int index = i;
      int cy = lbY + 6 + i * 18;
      net.minecraft.client.gui.components.Button btn =
          net.minecraft.client.gui.components.Button.builder(
                  Component.literal(""),
                  b -> selectedCategory = index
              )
              .bounds(lbX, cy - 2, lbW, 12)
              .build();
      btn.setAlpha(0f);
      this.addRenderableWidget(btn);
    }

    int cbX = startX + PANEL_WIDTH + 1 + INNER_PADDING;
    int cbY = startY + HEADER_HEIGHT + 1 + INNER_PADDING;
    int cbW = SCREEN_WIDTH - PANEL_WIDTH - 1 - INNER_PADDING * 2;

    int by = cbY + 26;
    int ty = cbY + 48;
    int toggleX = cbX + cbW - 36;
    net.minecraft.client.gui.components.Button toggleBtn =
        net.minecraft.client.gui.components.Button.builder(
                Component.literal(""),
                b -> {
                  ConfigManager.get().hudEnabled = !ConfigManager.get().hudEnabled;
                  ConfigManager.save();
                }
            )
            .bounds(toggleX, ty - 1, 28, 10)
            .build();
    toggleBtn.setAlpha(0f);
    this.addRenderableWidget(toggleBtn);

    net.minecraft.client.gui.components.Button hudPosBtn =
        net.minecraft.client.gui.components.Button.builder(
                Component.literal("Edit HUD Positions"),
                b -> minecraft.setScreen(new HudEditorScreen())
            )
            .bounds(cbX + cbW - 80, by - 2, 75, 12)
            .build();
    hudPosBtn.setAlpha(0f);
    this.addRenderableWidget(hudPosBtn);
  }

  /**
   * Renders the full config screen including the header, left category panel,
   * right content panel, and category-specific content.
   *
   * @param g      the graphics context
   * @param mouseX the current mouse X position
   * @param mouseY the current mouse Y position
   * @param delta  the render tick delta
   */
  @Override
  public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
    g.fill(0, 0, this.width, this.height, 0x99000000);

    int x = startX;
    int y = startY;
    int w = SCREEN_WIDTH;
    int h = SCREEN_HEIGHT;

    g.fill(x - 1, y - 1, x + w + 1, y + h + 1, BORDER);
    g.fill(x, y, x + w, y + h, OUTER_BG);

    g.fill(x, y, x + w, y + HEADER_HEIGHT, HEADER_BG);
    g.fill(x, y + HEADER_HEIGHT, x + w, y + HEADER_HEIGHT + 1, BORDER);
    int titleW = font.width("SkyblockRemaining");
    g.drawString(font, "SkyblockRemaining", x + (w - titleW) / 2, y + 6, TEXT, false);

    int bodyY = y + HEADER_HEIGHT + 1;
    int bodyH = h - HEADER_HEIGHT - 1;

    g.fill(x, bodyY, x + PANEL_WIDTH, y + h, PANEL_BG);
    g.fill(x + PANEL_WIDTH, bodyY, x + PANEL_WIDTH + 1, y + h, BORDER);
    g.fill(x + PANEL_WIDTH + 1, bodyY, x + w, y + h, CONTENT_BG);

    int lbX = x + INNER_PADDING;
    int lbY = bodyY + INNER_PADDING;
    int lbW = PANEL_WIDTH - INNER_PADDING * 2;
    int lbH = bodyH - INNER_PADDING * 2;
    g.fill(lbX - 1, lbY - 1, lbX + lbW + 1, lbY + lbH + 1, BORDER);
    g.fill(lbX, lbY, lbX + lbW, lbY + lbH, INNER_BG);

    for (int i = 0; i < categories.size(); i++) {
      int cy = lbY + 6 + i * 18;
      boolean selected = i == selectedCategory;
      boolean hovered = mouseX >= lbX && mouseX <= lbX + lbW
          && mouseY >= cy - 2 && mouseY <= cy + 10;

      if (selected) {
        g.fill(lbX, cy - 2, lbX + lbW, cy + 10, SELECTED_BG);
        g.fill(lbX + 4, cy + 11, lbX + lbW - 4, cy + 12, ACCENT);
      } else if (hovered) {
        g.fill(lbX, cy - 2, lbX + lbW, cy + 10, 0xFF222222);
      }

      int nameW = font.width(categories.get(i));
      int color = selected ? TEXT : SUBTEXT;
      g.drawString(font, categories.get(i), lbX + (lbW - nameW) / 2, cy, color, false);
    }

    int cbX = x + PANEL_WIDTH + 1 + INNER_PADDING;
    int cbY = bodyY + INNER_PADDING;
    int cbW = w - PANEL_WIDTH - 1 - INNER_PADDING * 2;
    int cbH = bodyH - INNER_PADDING * 2;
    g.fill(cbX - 1, cbY - 1, cbX + cbW + 1, cbY + cbH + 1, BORDER);
    g.fill(cbX, cbY, cbX + cbW, cbY + cbH, INNER_BG);

    String label = categories.get(selectedCategory);
    g.drawString(font, label, cbX + 8, cbY + 8, TEXT, false);
    g.fill(cbX + 4, cbY + 19, cbX + cbW - 4, cbY + 20, BORDER);

    if (selectedCategory == 0) {
      int ay = cbY + 30;
      String versionLine = "SkyblockRemaining v" + modVersion;
      int vw = font.width(versionLine);
      g.drawString(font, versionLine, cbX + (cbW - vw) / 2, ay, ACCENT, false);
      ay += 14;
      var lines = font.split(net.minecraft.network.chat.Component.literal(
          "A Hypixel Skyblock HUD mod providing quality-of-life overlays."
      ), cbW - 16);
      for (var line : lines) {
        g.drawString(font, line, cbX + 8, ay, SUBTEXT, false);
        ay += 10;
      }
    } else {
      int by = cbY + 26;
      int ty = cbY + 48;
      boolean enabled = ConfigManager.get().hudEnabled;
      g.drawString(font, "Show Coordinates HUD", cbX + 8, ty, TEXT, false);
      int toggleX = cbX + cbW - 36;
      g.fill(toggleX, ty - 1, toggleX + 28, ty + 9, enabled ? 0xFF2D7D46 : 0xFF555555);
      g.fill(enabled ? toggleX + 16 : toggleX + 2, ty, enabled ? toggleX + 26 : toggleX + 12,
          ty + 8, 0xFFFFFFFF);
      g.drawString(font, enabled ? "ON" : "OFF", enabled ? toggleX + 3 : toggleX + 13, ty,
          0xFFFFFFFF, false);

      g.drawString(font, "Edit HUD Positions", cbX + 8, by, TEXT, false);
      int btnX = cbX + cbW - 80;
      g.fill(btnX, by - 2, btnX + 75, by + 10, BORDER);
      g.fill(btnX + 1, by - 1, btnX + 74, by + 9, 0xFF2A2A2A);
      int editW = font.width("Edit");
      g.drawString(font, "Edit", btnX + (75 - editW) / 2, by, SUBTEXT, false);
    }

    super.render(g, mouseX, mouseY, delta);
  }

  /**
   * Returns false to prevent the game from pausing when this screen is open.
   */
  @Override
  public boolean isPauseScreen() {
    return false;
  }
}
