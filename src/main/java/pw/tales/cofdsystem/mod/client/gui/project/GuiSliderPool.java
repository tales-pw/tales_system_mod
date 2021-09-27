package pw.tales.cofdsystem.mod.client.gui.project;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiSlider;

public class GuiSliderPool extends GuiSlider {

  @SuppressWarnings("java:S107")
  public GuiSliderPool(IPoolCallback pI455411, int pI455412, int pI455413, int pI455414,
      String pI455415, float pI455416, float pI455417, float pI455418, FormatHelper pI455419) {
    super(pI455411, pI455412, pI455413, pI455414, pI455415, pI455416, pI455417, pI455418, pI455419);
  }

  interface IPoolCallback extends GuiPageButtonList.GuiResponder {

    default void setEntryValue(int i, boolean b) {

    }

    default void setEntryValue(int i, float v) {
      this.setEntryValue(i, (int) v);
    }

    void setEntryValue(int i, int v);

    @ParametersAreNonnullByDefault
    default void setEntryValue(int i, String s) {

    }
  }
}
