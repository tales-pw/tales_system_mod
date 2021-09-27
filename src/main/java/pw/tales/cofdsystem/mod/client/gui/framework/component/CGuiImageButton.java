package pw.tales.cofdsystem.mod.client.gui.framework.component;

import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class CGuiImageButton extends CGuiButton {

  private final ResourceLocation resource;

  public CGuiImageButton(ResourceLocation resource, Consumer<GuiButton> runnable) {
    this(0, resource, runnable);
  }

  public CGuiImageButton(int id, ResourceLocation resource, Consumer<GuiButton> runnable) {
    super(id, "", runnable);
    this.resource = resource;
  }

  public CGuiImageButton(int id, ResourceLocation resourceLocation) {
    this(id, resourceLocation, DO_NOTING);
  }


  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
    super.drawButton(mc, mouseX, mouseY, partialTicks);
    if (this.visible) {
      int p = 2;

      int height = this.height - 2 * p;
      int width = this.width - 2 * p;

      mc.renderEngine.bindTexture(resource);
      drawModalRectWithCustomSizedTexture(this.x + p, this.y + p, 0, 0, width, height, width,
          height);
    }
  }
}
