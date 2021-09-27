package pw.tales.cofdsystem.mod.client.gui.project;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiButtonImage extends GuiButton {

  public static final int SIZE = 20;
  private final ResourceLocation resource;

  public GuiButtonImage(int buttonId, int x, int y, ResourceLocation resource) {
    super(buttonId, x, y, SIZE, SIZE, "");
    this.resource = resource;
  }

  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
    super.drawButton(mc, mouseX, mouseY, partialTicks);
    if (this.visible) {
      int innerPadding = 2;

      int height = this.height - 2 * innerPadding;
      int width = this.width - 2 * innerPadding;

      mc.renderEngine.bindTexture(resource);
      drawModalRectWithCustomSizedTexture(this.x + innerPadding, this.y + innerPadding, 0, 0, width,
          height, width, height);
    }
  }
}

