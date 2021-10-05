package pw.tales.cofdsystem.mod.client.modules.gui_windows.gui;

import static pw.tales.cofdsystem.mod.client.modules.gui_windows.gui.GuiTabContainer.addAlpha;

import java.awt.Color;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class GuiTab extends Gui {

  public static final int MAX_LINES = 14;
  public static final int DEFAULT_COLOR = 16777215;
  public static final int BACKGROUND_COLOR = addAlpha(
      Color.black.brighter(),
      180
  ).getRGB();

  public static final int PADDING = 2;

  private final Minecraft mc;

  private final int left;
  private final int top;

  private final GuiTabButton tabButton;
  private final Consumer<ITextComponent> openCallback;

  private List<ITextComponent> components;

  public GuiTab(
      Minecraft minecraft,
      GuiTabButton tabButton,
      Consumer<ITextComponent> openCallback
  ) {
    this.mc = minecraft;
    this.left = tabButton.getSize() + PADDING;
    this.top = 0;
    this.tabButton = tabButton;
    this.openCallback = openCallback;
  }

  public void update(ITextComponent component) {
    this.components = GuiUtilRenderComponents.splitText(
        component,
        this.mc.displayWidth,
        this.mc.fontRenderer,
        false,
        false
    );
  }

  public void draw() {
    FontRenderer renderer = this.mc.fontRenderer;

    float scale = this.getScale();

    GlStateManager.pushMatrix();
    GlStateManager.scale(scale, scale, 1.0F);

    GlStateManager.translate(this.left, this.top, 0);
    GlStateManager.translate(PADDING, PADDING, 0);

    GlStateManager.enableBlend();

    for (int i = 0; i < components.size(); i++) {
      ITextComponent component = components.get(i);
      String formattedText = component.getFormattedText();

      renderer.drawStringWithShadow(
          formattedText,
          0,
          (float) i * renderer.FONT_HEIGHT,
          DEFAULT_COLOR
      );
    }

    GlStateManager.disableAlpha();
    GlStateManager.disableBlend();

    GlStateManager.popMatrix();
  }

  public float getScale() {
    return this.mc.gameSettings.chatScale;
  }

  public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
    if (mouseButton != 0) {
      return false;
    }

    ITextComponent itextcomponent = this.getComponent(mouseX, mouseY);
    if (itextcomponent != null) {
      this.openCallback.accept(itextcomponent);
      return true;
    }

    return false;
  }

  @Nullable
  public ITextComponent getComponent(int mouseX, int mouseY) {
    ImmutablePair<Integer, Integer> scaledMouse = this.getScaledMouseClick(
        mouseX,
        mouseY
    );

    int scaledMouseX = scaledMouse.getLeft();
    int scaledMouseY = scaledMouse.getRight();

    if (!isMouseHovering(scaledMouseX, scaledMouseY)) {
      return null;
    }

    FontRenderer fontRenderer = this.mc.fontRenderer;

    int lineClicked = (scaledMouseY - top) / fontRenderer.FONT_HEIGHT;

    if (lineClicked >= components.size() || lineClicked < 0) {
      return null;
    }

    ITextComponent component = components.get(lineClicked);

    int cursor = this.left;
    for (ITextComponent iTextComponent : component) {
      if (!(iTextComponent instanceof TextComponentString)) {
        continue;
      }

      TextComponentString textComponent = (TextComponentString) iTextComponent;

      cursor += this.getComponentWidth(textComponent);

      if (cursor > mouseX) {
        return iTextComponent;
      }
    }

    return null;
  }

  public int getComponentWidth(TextComponentString textComponent) {
    return this.mc.fontRenderer.getStringWidth(
        GuiUtilRenderComponents.removeTextColorsIfConfigured(
            textComponent.getText(),
            false
        )
    );
  }

  private boolean isMouseHovering(int scaledMouseX, int scaledMouseY) {
    if (scaledMouseX < left || scaledMouseY < top) {
      return false;
    }

    return scaledMouseY <= top + this.getScaledHeight();
  }

  public int getScaledHeight() {
    return MathHelper.floor(this.getHeight() / this.getScale());
  }

  private int getHeight() {
    return this.components.size() * this.mc.fontRenderer.FONT_HEIGHT + 2 * PADDING;
  }

  private ImmutablePair<Integer, Integer> getScaledMouseClick(int mouseX, int mouseY) {
    float chatScale = this.getScale();

    return new ImmutablePair<>(
        MathHelper.floor(mouseX / chatScale),
        MathHelper.floor(mouseY - 1.5f / chatScale)
    );
  }

  public GuiTabButton getTabButton() {
    return tabButton;
  }

  public void drawBackground() {
    drawGradientRect(
        0,
        0,
        mc.displayWidth,
        MAX_LINES * mc.fontRenderer.FONT_HEIGHT + PADDING,
        BACKGROUND_COLOR,
        BACKGROUND_COLOR
    );
  }
}
