package pw.tales.cofdsystem.mod.client.gui.framework.layout;

import java.util.ArrayList;
import java.util.List;
import pw.tales.cofdsystem.mod.client.gui.framework.component.IComponent;

public abstract class Layout implements IComponent {

  private final List<IComponent> components = new ArrayList<>();

  private int width = 0;
  private int height = 0;
  private int padding = 0;
  private int x = 0;
  private int y = 0;
  private boolean hidden = false;

  public void arrangeAll() {
    for (IComponent component : components) {
      this.arrange(component);

      if (component instanceof Layout) {
        Layout layout = (Layout) component;
        layout.arrangeAll();
      }
    }
  }

  public abstract void arrange(IComponent component);

  public void addComponent(IComponent component) {
    this.components.add(component);
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
  public void drawComponent(int mouseX, int mouseY, float partialTicks) {
    if (hidden) {
      return;
    }

    for (IComponent component : components) {
      component.drawComponent(mouseX, mouseY, partialTicks);
    }
  }

  @Override
  public void mouseComponentClicked(int mouseX, int mouseY, int mouseButton) {
    for (IComponent component : components) {
      component.mouseComponentClicked(mouseX, mouseY, mouseButton);
    }
  }

  @Override
  public void mouseComponentReleased(int mouseX, int mouseY) {
    for (IComponent component : components) {
      component.mouseComponentReleased(mouseX, mouseY);
    }
  }

  @Override
  public void handleComponentMouseInput(int mouseX, int mouseY) {
    for (IComponent component : components) {
      component.handleComponentMouseInput(mouseX, mouseY);
    }
  }

  @Override
  public void keyComponentTyped(char typedChar, int keyCode) {
    for (IComponent component : components) {
      component.keyComponentTyped(typedChar, keyCode);
    }
  }

  public boolean getHidden() {
    return this.hidden;
  }

  public void setHidden(boolean isHidden) {
    this.hidden = isHidden;
  }

  protected int getPadding() {
    return padding;
  }

  public void setPadding(int padding) {
    this.padding = padding;
  }

  public void clear() {
    components.clear();
  }
}
