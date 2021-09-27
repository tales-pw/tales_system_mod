package pw.tales.cofdsystem.mod.client.gui.framework.component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

public class CGuiList extends GuiScrollingList implements IComponent {

  protected List<String> displayList = new ArrayList<>();
  protected Set<String> selectedList = new HashSet<>();

  private ClickHook elementClickHook = (value -> {
  });

  @SuppressWarnings("deprecation")
  public CGuiList(int width, int height, int x, int y) {
    super(Minecraft.getMinecraft(), width, height, y, y + height, x, 20);
  }

  public void setDisplayList(List<String> displayList) {
    this.displayList = displayList;
  }

  @Override
  protected int getSize() {
    return displayList.size();
  }

  @Override
  protected void elementClicked(int index, boolean doubleClick) {
    String value = displayList.get(index);

    if (selectedList.contains(value)) {
      selectedList.remove(value);
    } else {
      selectedList.add(value);
    }

    elementClickHook.execute(value);
  }

  @Override
  protected boolean isSelected(int index) {
    String value = displayList.get(index);
    return selectedList.contains(value);
  }

  @Override
  protected void drawBackground() {
    this.drawGradientRect(this.left, this.top, this.right, this.bottom, 0xC0101010, 0xD0101010);
  }

  @Override
  protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer,
      Tessellator tess) {
    String value = displayList.get(slotIdx);

    FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    fontRenderer
        .drawString(fontRenderer.trimStringToWidth(value, listWidth - 10), this.left + 5,
            slotTop + fontRenderer.FONT_HEIGHT / 2, 0xFFFFFF);
  }

  @Override
  public int getComponentX() {
    return this.left;
  }

  @Override
  public void setComponentX(int x) {
    this.setLeft(x);
    this.setRight(x + this.getComponentWidth());
  }

  private void setLeft(int value) {
    this.setFinalField("left", value);
  }

  @SuppressWarnings("java:S3011")
  private void setFinalField(String name, int value) {
    try {
      Field left = CGuiList.class.getField(name);
      left.setAccessible(true);
      left.set(this, value);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  private void setRight(int value) {
    this.setFinalField("left", value);
  }

  @Override
  public int getComponentWidth() {
    return this.listWidth;
  }

  @Override
  public void setComponentWidth(int width) {
    this.setListWidth(width);
    this.setRight(this.left + width);
  }

  private void setListWidth(int value) {
    this.setFinalField("listWidth", value);
  }

  @Override
  public int getComponentY() {
    return this.top;
  }

  @Override
  public void setComponentY(int y) {
    this.setTop(y);
    this.setBottom(y + this.getComponentHeight());
  }

  private void setTop(int value) {
    this.setFinalField("top", value);
  }

  private void setBottom(int value) {
    this.setFinalField("bottom", value);
  }

  @Override
  public int getComponentHeight() {
    return this.listHeight;
  }

  @Override
  public void setComponentHeight(int height) {
    this.setListHeight(height);
    this.setBottom(this.top + height);
  }

  private void setListHeight(int value) {
    this.setFinalField("listHeight", value);
  }

  @Override
  public void drawComponent(int mouseX, int mouseY, float renderPartialTicks) {
    this.drawScreen(mouseX, mouseY, renderPartialTicks);
  }

  @Override
  public void handleComponentMouseInput(int mouseX, int mouseY) {
    try {
      this.handleMouseInput(mouseX, mouseY);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Set<String> getSelectedList() {
    return selectedList;
  }

  public void setSelectedList(Set<String> selectedList) {
    this.selectedList = selectedList;
  }

  public void setElementClickHook(ClickHook o) {
    this.elementClickHook = o;
  }

  public interface ClickHook {

    void execute(String value);
  }
}
