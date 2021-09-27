package pw.tales.cofdsystem.mod.client.modules.gui_windows.gui;

import java.awt.Color;
import java.util.function.BooleanSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class GuiTabButton extends Gui {

  public static final int BORDER_WIDTH = 1;

  private final int x;
  private final int startY;
  private final int size;

  private final Minecraft mc;
  private final BooleanSupplier openedSupplier;
  private final BooleanSupplier updatedSupplier;

  private final Runnable toggleCallback;
  private final Runnable removeCallback;

  public GuiTabButton(
      Minecraft mc,
      BooleanSupplier openedSupplier,
      BooleanSupplier updatedSupplier,
      Runnable toggleCallback,
      Runnable removeCallback
  ) {
    this.mc = mc;

    this.x = 0;
    this.startY = 0;
    this.size = 2 * mc.fontRenderer.FONT_HEIGHT;

    this.openedSupplier = openedSupplier;
    this.updatedSupplier = updatedSupplier;
    this.toggleCallback = toggleCallback;
    this.removeCallback = removeCallback;
  }

  /**
   * @param shiftNumber  Position on the screen.
   * @param actualNumber Position in full button list.
   */
  public void draw(int shiftNumber, int actualNumber) {
    int shiftY = this.getShiftY(shiftNumber);

    this.drawGradientRect(
        x,
        shiftY,
        x + size,
        shiftY + size,
        Color.WHITE.getRGB(),
        Color.WHITE.getRGB()
    );

    Color backgroundColor = Color.BLACK;
    if (this.openedSupplier.getAsBoolean()) {
      backgroundColor = Color.LIGHT_GRAY;
    } else if (this.updatedSupplier.getAsBoolean()) {
      backgroundColor = Color.YELLOW;
    }

    this.drawGradientRect(
        x + BORDER_WIDTH,
        shiftY + BORDER_WIDTH,
        x + size - BORDER_WIDTH,
        shiftY + size - BORDER_WIDTH,
        backgroundColor.getRGB(),
        backgroundColor.getRGB()
    );

    this.drawCenteredString(
        this.mc.fontRenderer,
        String.valueOf(actualNumber),
        x + size / 2,
        shiftY + size / 2 - this.mc.fontRenderer.FONT_HEIGHT / 2,
        Color.WHITE.getRGB()
    );
  }

  public int getShiftY(int shiftNumber) {
    return startY + shiftNumber * size;
  }

  public boolean mouseClicked(int shiftNumber, int mouseX, int mouseY, int mouseButton) {
    boolean mouseHovering = isMouseHovering(shiftNumber, mouseX, mouseY);

    if (mouseButton == 0 && mouseHovering) {
      this.toggleCallback.run();
      return true;
    }

    if (mouseButton == 1 && mouseHovering) {
      this.removeCallback.run();
      return true;
    }

    return false;
  }

  public boolean isMouseHovering(int shiftNumber, int mouseX, int mouseY) {
    int y = this.getShiftY(shiftNumber);

    if (mouseX < x || mouseY < y) {
      return false;
    }

    if (mouseX > x + this.size) {
      return false;
    }

    if (mouseY > y + this.size) {
      return false;
    }

    return true;
  }

  public int getSize() {
    return size;
  }
}
