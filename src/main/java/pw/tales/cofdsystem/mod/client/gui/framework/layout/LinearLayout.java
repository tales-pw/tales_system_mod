package pw.tales.cofdsystem.mod.client.gui.framework.layout;

import pw.tales.cofdsystem.mod.client.gui.framework.component.IComponent;

public class LinearLayout extends Layout {

  private int shift;

  @Override
  public void arrange(IComponent component) {
    int padding = this.getPadding();

    component.setComponentWidth(this.getComponentWidth() - 2 * padding);
    component.setComponentX(this.getComponentX() + padding);
    component.setComponentY(this.getComponentY() + this.shift);

    this.shift += component.getComponentHeight() + padding;
  }

  @Override
  public void arrangeAll() {
    shift = this.getPadding();
    super.arrangeAll();
  }
}
