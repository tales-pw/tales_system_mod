package pw.tales.cofdsystem.mod.client.gui.project;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.util.ResourceLocation;
import pw.tales.cofdsystem.dices.EnumExplode;
import pw.tales.cofdsystem.dices.IRollRequest;
import pw.tales.cofdsystem.dices.requests.RollRequestPool;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.client.gui.EnumExplodeAdapter;

public class GuiButtonRoll extends GuiButtonImage {

  private static final ResourceLocation location = new ResourceLocation(TalesSystem.MOD_ID,
      "textures/gui/rolling-dices.png");
  private final IRollHandler handler;
  private final GuiSlider dicePoolSlider;
  private final GuiButtonEnum<EnumExplodeAdapter> explodeButton;
  private boolean opened = false;
  private int pool = 10;
  private EnumExplode explode;

  public GuiButtonRoll(IRollHandler handler, int buttonId, int x, int y) {
    super(buttonId, x, y, location);
    this.handler = handler;
    final int width = 3 * GuiButtonImage.SIZE;
    this.dicePoolSlider = new GuiSliderPool(
        (i, v) -> this.pool = v,
        -1,
        x - GuiButtonImage.SIZE - width, y,
        "",
        0, 50,
        this.pool,
        (id, name, value) -> Integer.toString((int) value)
    );
    this.dicePoolSlider.width = width;
    this.dicePoolSlider.height = GuiButtonImage.SIZE;
    this.dicePoolSlider.visible = false;

    this.explodeButton = new GuiButtonEnum<>(
        (i, v) -> this.explode = v.getExplode(),
        -1,
        x - GuiButtonImage.SIZE, y,
        GuiButtonImage.SIZE, GuiButtonImage.SIZE,
        EnumExplodeAdapter.DEFAULT
    );
    this.explodeButton.visible = false;
    this.explode = this.explodeButton.getValue().getExplode();
  }

  @Override
  public void mouseReleased(int pMouseReleased1, int pMouseReleased2) {
    super.mouseReleased(pMouseReleased1, pMouseReleased2);
    this.dicePoolSlider.mouseReleased(pMouseReleased1, pMouseReleased2);
    this.explodeButton.mouseReleased(pMouseReleased1, pMouseReleased2);
  }

  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pollPressed = this.dicePoolSlider.mousePressed(mc, mouseX, mouseY);
    boolean explodePressed = this.explodeButton.mousePressed(mc, mouseX, mouseY);
    boolean buttonPressed = super.mousePressed(mc, mouseX, mouseY);

    if (buttonPressed && this.isOpened()) {
      this.handler.roll(this.buildRequest());
    }

    boolean isAnythingPressed = buttonPressed || pollPressed || explodePressed;
    this.setOpened(isAnythingPressed);

    return isAnythingPressed;
  }

  public IRollRequest buildRequest() {
    RollRequestPool rollRequestPool = new RollRequestPool(this.pool);
    rollRequestPool.explode = this.explode;
    return rollRequestPool;
  }

  public boolean isOpened() {
    return opened;
  }

  public void setOpened(boolean opened) {
    this.opened = opened;
    this.dicePoolSlider.visible = opened;
    this.explodeButton.visible = opened;
  }

  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
    super.drawButton(mc, mouseX, mouseY, partialTicks);
    this.dicePoolSlider.drawButton(mc, mouseX, mouseY, partialTicks);
    this.explodeButton.drawButton(mc, mouseX, mouseY, partialTicks);
  }

  public interface IRollHandler {

    void roll(IRollRequest request);
  }
}
