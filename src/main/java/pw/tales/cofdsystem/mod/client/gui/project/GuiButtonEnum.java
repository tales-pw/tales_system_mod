package pw.tales.cofdsystem.mod.client.gui.project;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

public class GuiButtonEnum<T extends GuiButtonEnum.IOrderedValue<T>> extends GuiButton {

  public static final Color TOOLTIP_BACKGROUND = new Color(0, 0, 0, 180);

  private T value;
  private String tooltip;
  private IEnumResponder<T> responder = (i, v) -> {
  };

  public GuiButtonEnum(IEnumResponder<T> responder, int buttonId, int x, int y, int width,
      int height, T value) {
    super(buttonId, x, y, width, height, "");
    this.value = value;
    this.responder = responder;
    this.update();
  }

  private void update() {
    this.responder.setEntryValue(this.id, this.value);
    this.displayString = I18n.format(String.format("%s.short", this.value.getTranslationKey()));
    this.tooltip = I18n.format(this.value.getTranslationKey());
  }


  public GuiButtonEnum(int buttonId, int x, int y, int width, int height, T value) {
    super(buttonId, x, y, width, height, "");
    this.value = value;
    this.update();
  }

  public GuiButtonEnum(int buttonId, int x, int y, T value) {
    super(buttonId, x, y, "");
    this.value = value;
  }

  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
    super.drawButton(mc, mouseX, mouseY, partialTicks);
    if (isMouseOver()) {
      this.drawTooltip(mc.fontRenderer);
    }
  }

  public void drawTooltip(FontRenderer fontRendererIn) {
    int stringWidth = fontRendererIn.getStringWidth(this.tooltip);

    int left = this.x + this.width / 2 - stringWidth / 2;
    int top = this.y + this.height;
    int right = left + stringWidth;
    int bottom = top + fontRendererIn.FONT_HEIGHT;

    this.drawGradientRect(
        left - 1,
        top,
        right + 2,
        bottom + 2,
        TOOLTIP_BACKGROUND.getRGB(),
        TOOLTIP_BACKGROUND.getRGB()
    );

    fontRendererIn.drawStringWithShadow(
        this.tooltip,
        (float) left + 1,
        (float) top + 1,
        Color.WHITE.getRGB()
    );
  }

  public T getValue() {
    return value;
  }

  public void setValue(T enumValue) {
    this.value = enumValue;
    update();
  }

  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    if (super.mousePressed(mc, mouseX, mouseY)) {
      this.setValue(value.getNext());
      return true;
    }

    return false;
  }

  public interface IOrderedValue<T> {

    T getNext();

    default String getTranslationKey() {
      return String.format("enum.%s.%s", this.getEnumName(), this.getValueName());
    }

    String getEnumName();

    String getValueName();
  }

  public interface IEnumResponder<T> {

    void setEntryValue(int i, T b);
  }
}
