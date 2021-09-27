package pw.tales.cofdsystem.mod.client.gui.framework.component;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiSlider;

public class CGuiRange extends GuiSlider implements IComponent {

  public CGuiRange(int id, String prefix, String suffix, double min, double max, double current) {
    this(id, prefix, suffix, min, max, current, false, true);
  }

  @java.lang.SuppressWarnings("java:S107")
  public CGuiRange(int id, String prefix, String suffix, double min, double max, double current,
      boolean showDec, boolean drawStr) {
    super(id, 0, 0, 0, 20, prefix, suffix, min, max, current, showDec, drawStr);
    precision = 0;
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
    Minecraft mc = Minecraft.getMinecraft();
    this.drawButton(mc, mouseX, mouseY, renderPartialTicks);
  }

  @Override
  public void mouseComponentClicked(int mouseX, int mouseY, int mouseButton) {
    Minecraft mc = Minecraft.getMinecraft();
    this.mousePressed(mc, mouseX, mouseY);
  }

  @Override
  public void mouseComponentReleased(int mouseX, int mouseY) {
    this.mouseReleased(mouseX, mouseY);
  }
}
