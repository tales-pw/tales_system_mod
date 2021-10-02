package pw.tales.cofdsystem.mod.client.modules.gui_windows;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pw.tales.cofdsystem.mod.client.modules.gui_system.gui.GuiSystem;
import pw.tales.cofdsystem.mod.client.modules.gui_windows.gui.GuiTabContainer;
import pw.tales.cofdsystem.mod.common.IModule;

@Singleton
public class ClientWindowsModule implements IModule {

  private final GuiTabContainer guiTabContainer;
  private final Minecraft mc;

  @Inject
  public ClientWindowsModule(Minecraft minecraft) {
    this.mc = minecraft;
    this.guiTabContainer = new GuiTabContainer();
  }

  @Override
  public void setUp() {
    EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
    if (isSystemGuiOpened()) {
      return;
    }

    if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
      return;
    }

    this.guiTabContainer.render();
  }

  public boolean isSystemGuiOpened() {
    return this.mc.currentScreen instanceof GuiSystem;
  }

  public GuiTabContainer getGuiWindows() {
    return guiTabContainer;
  }

  public void update(String id, ITextComponent component, boolean forceOpen) {
    this.guiTabContainer.update(id, component, forceOpen);
  }

  public void remove(String id) {
    this.guiTabContainer.remove(id);
  }
}
