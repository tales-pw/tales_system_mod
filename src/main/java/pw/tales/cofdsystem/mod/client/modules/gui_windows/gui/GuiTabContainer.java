package pw.tales.cofdsystem.mod.client.modules.gui_windows.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.input.Mouse;

public class GuiTabContainer extends GuiScreen {

  public static final int MAX_TAB_LINES = 6;

  private final Map<String, GuiTab> tabs = new HashMap<>();

  private final List<String> order = new ArrayList<>();
  private final Set<String> updated = new HashSet<>();
  private String openedId = null;

  private int scrollShift = 0;

  private boolean mouseIntercepted;

  public static Color addAlpha(Color color, int alpha) {
    return new Color(
        color.getRed(),
        color.getGreen(),
        color.getBlue(),
        alpha
    );
  }

  public boolean isOpened(String id) {
    return Objects.equals(this.openedId, id);
  }

  public boolean isUpdated(String id) {
    return this.updated.contains(id);
  }

  public GuiTab ensureTab(String id) {
    GuiTab guiTab = this.tabs.get(id);

    if (!Objects.equals(guiTab, null)) {
      return guiTab;
    }

    GuiTabButton tabButton = new GuiTabButton(
        this.mc,
        () -> this.isOpened(id),
        () -> this.isUpdated(id),
        () -> this.toggle(id),
        () -> this.remove(id)
    );

    guiTab = new GuiTab(
        this.mc,
        tabButton,
        this::handleComponentClick
    );

    this.tabs.put(id, guiTab);
    this.order.add(id);

    return guiTab;
  }

  public void update(
      String id,
      ITextComponent component,
      boolean forceOpen
  ) {
    GuiTab tab = this.ensureTab(id);

    // Update content
    tab.update(component);

    if (forceOpen) {
      this.open(id);
      this.scrollTo(id);
    }

    if (!this.isOpened(id)) {
      this.updated.add(id);
    }
  }

  public void remove(String id) {
    // Close if opened
    this.close(id);

    // Clean up tabs
    this.tabs.remove(id);
    this.order.remove(id);
  }

  public void toggle(String id) {
    if (this.isOpened(id)) {
      this.close(id);
    } else {
      this.open(id);
    }
  }

  public void close(String id) {
    if (this.isOpened(id)) {
      this.openedId = null;
    }
  }

  public void open(String id) {
    this.openedId = id;
    this.updated.remove(id);
  }

  public void render() {
    this.drawScreen(0, 0, 0);
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    this.drawBackground(0);
    this.drawTab();
    this.drawTabButtons();
    this.drawHoveringText(mouseX, mouseY);
  }

  private void drawTab() {
    GuiTab tab = this.getOpenedTab();
    if (tab == null) {
      return;
    }

    tab.draw();
  }

  private void drawHoveringText(int mouseX, int mouseY) {
    GuiTab tab = this.getOpenedTab();
    if (tab == null) {
      return;
    }

    this.handleWindowHovering(tab, mouseX, mouseY);
  }

  public void handleWindowHovering(GuiTab window, int mouseX, int mouseY) {
    ITextComponent hoveringComponent = window.getComponent(mouseX, mouseY);
    if (hoveringComponent != null) {
      this.handleComponentHover(hoveringComponent, mouseX, mouseY);
    }
  }

  private void drawTabButtons() {
    int i = 0;
    for (GuiTabButton tabButton : this.getVisibleButtons()) {
      tabButton.draw(i++, this.scrollShift + i);
    }
  }

  private List<GuiTabButton> getVisibleButtons() {
    int start = scrollShift;
    int end = this.getLastItem();

    return this.order.subList(start, end).stream().map(
        id -> this.tabs.get(id).getTabButton()
    ).collect(Collectors.toList());
  }

  private int getLastItem() {
    return Math.min(
        this.scrollShift + MAX_TAB_LINES,
        this.order.size()
    );
  }

  @Override
  public void drawBackground(int tint) {
    GuiTab tab = this.getOpenedTab();
    if (tab == null) {
      return;
    }

    tab.drawBackground();
  }

  @Nullable
  public GuiTab getOpenedTab() {
    return this.tabs.get(this.openedId);
  }

  private void scrollTo(String id) {
    int index = this.order.indexOf(id);
    this.scrollTo(index);
  }

  private void scrollTo(int target) {
    this.scrollShift = this.normalizeShift(
        target - MAX_TAB_LINES / 2
    );
  }

  @Override
  public void handleMouseInput() throws IOException {
    this.mouseIntercepted = false;

    super.handleMouseInput();
    this.handleTabButtonsScroll();
  }

  private void handleTabButtonsScroll() {
    int i = Mouse.getEventDWheel();

    if (i == 0) {
      return;
    }

    int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
    int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

    if (!this.isMouseOverTabButtons(mouseX, mouseY)) {
      return;
    }

    this.scroll(i);

    this.mouseIntercepted = true;
  }

  private void scroll(int amount) {
    if (amount > 1) {
      amount = 1;
    }

    if (amount < -1) {
      amount = -1;
    }

    this.scrollShift = normalizeShift(this.scrollShift - amount);
  }

  public int normalizeShift(int shift) {
    shift = Math.max(shift, 0);
    shift = Math.min(shift, this.getMaxShift());
    return shift;
  }

  public int getMaxShift() {
    int size = this.order.size();
    return Math.max(
        0,
        size - MAX_TAB_LINES
    );
  }

  public boolean isMouseOverTabButtons(int mouseX, int mouseY) {
    int size = mc.fontRenderer.FONT_HEIGHT * 2;
    return mouseX < size || mouseY < 2 * size;
  }

  @Override
  public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    if (this.handleOpenedTabClicked(mouseX, mouseY, mouseButton)) {
      this.mouseIntercepted = true;
      return;
    }

    if (this.handleTabButtonClicked(mouseX, mouseY, mouseButton)) {
      this.mouseIntercepted = true;
      return;
    }
  }

  public boolean handleOpenedTabClicked(int mouseX, int mouseY, int mouseButton) {
    GuiTab openedTab = this.getOpenedTab();

    if (openedTab == null) {
      return false;
    }

    return openedTab.mouseClicked(mouseX, mouseY, mouseButton);
  }

  public boolean handleTabButtonClicked(int mouseX, int mouseY, int mouseButton) {
    int i = 0;

    for (GuiTabButton tabButton : this.getVisibleButtons()) {
      if (tabButton.mouseClicked(i++, mouseX, mouseY, mouseButton)) {
        return true;
      }
    }

    return false;
  }

  public boolean isMouseIntercepted() {
    return mouseIntercepted;
  }
}
