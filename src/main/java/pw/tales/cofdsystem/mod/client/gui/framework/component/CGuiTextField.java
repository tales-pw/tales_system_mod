package pw.tales.cofdsystem.mod.client.gui.framework.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;

public class CGuiTextField extends GuiTextField implements IComponent {

  public CGuiTextField(int componentId) {
    super(componentId, Minecraft.getMinecraft().fontRenderer, 0, 0, 20, 20);
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
    super.drawTextBox();
  }

  @Override
  public void mouseComponentClicked(int mouseX, int mouseY, int mouseButton) {
    this.mouseClicked(mouseX, mouseY, mouseButton);
  }

  @Override
  public void keyComponentTyped(char typedChar, int keyCode) {
    this.textboxKeyTyped(typedChar, keyCode);
  }
}
