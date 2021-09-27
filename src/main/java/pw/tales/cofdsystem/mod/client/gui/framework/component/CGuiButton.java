package pw.tales.cofdsystem.mod.client.gui.framework.component;

import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class CGuiButton extends GuiButton implements IComponent {

  protected static final Consumer<GuiButton> DO_NOTING = button -> {
  };

  private final Consumer<GuiButton> clickCallback;

  public CGuiButton(String buttonText, Consumer<GuiButton> runnable) {
    this(0, buttonText, runnable);
  }

  public CGuiButton(int buttonId, String buttonText, Consumer<GuiButton> runnable) {
    super(buttonId, 0, 0, buttonText);
    this.clickCallback = runnable;
  }

  public CGuiButton(int buttonId, String buttonText) {
    this(buttonId, buttonText, DO_NOTING);
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
    if (this.mousePressed(mc, mouseX, mouseY)) {
      clickCallback.accept(this);
    }
  }
}
