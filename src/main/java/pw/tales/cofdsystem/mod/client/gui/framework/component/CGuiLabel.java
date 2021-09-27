package pw.tales.cofdsystem.mod.client.gui.framework.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiLabel;

public class CGuiLabel extends GuiLabel implements IComponent {

  public CGuiLabel(int id, int color) {
    super(Minecraft.getMinecraft().fontRenderer, id, 0, 0, 10, 20, color);
  }

  @Override
  public int getComponentX() {
    return this.x;
  }

  @Override
  public void setComponentX(int x) {
    this.x = x;
  }

  @Override
  public int getComponentY() {
    return this.y;
  }

  @Override
  public void setComponentY(int y) {
    this.y = y;
  }

  @Override
  public int getComponentWidth() {
    return this.width;
  }

  @Override
  public void setComponentWidth(int width) {
    this.width = width;
  }

  @Override
  public int getComponentHeight() {
    return this.height;
  }

  @Override
  public void setComponentHeight(int height) {
    this.height = height;
  }

  @Override
  public void drawComponent(int mouseX, int mouseY, float renderPartialTicks) {
    this.drawLabel(Minecraft.getMinecraft(), mouseX, mouseY);
  }
}
