package pw.tales.cofdsystem.mod.client.gui.framework.component;

public interface IComponent {

  int getComponentX();

  void setComponentX(int x);

  int getComponentY();

  void setComponentY(int y);

  int getComponentWidth();

  void setComponentWidth(int width);

  int getComponentHeight();

  void setComponentHeight(int height);

  void drawComponent(int mouseX, int mouseY, float renderPartialTicks);

  default void mouseComponentClicked(int mouseX, int mouseY, int mouseButton) {
  }

  default void mouseComponentReleased(int mouseX, int mouseY) {
  }

  default void handleComponentMouseInput(int mouseX, int mouseY) {
  }

  default void keyComponentTyped(char typedChar, int keyCode) {
  }
}
