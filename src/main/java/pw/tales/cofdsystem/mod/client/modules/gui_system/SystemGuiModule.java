package pw.tales.cofdsystem.mod.client.modules.gui_system;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import pw.tales.cofdsystem.mod.client.modules.gui_system.gui.GuiSystem;
import pw.tales.cofdsystem.mod.client.modules.gui_windows.ClientWindowsModule;
import pw.tales.cofdsystem.mod.client.modules.targets.TargetsList;
import pw.tales.cofdsystem.mod.client.modules.targets.gui.GuiTargets;
import pw.tales.cofdsystem.mod.common.IModule;

@Singleton
public class SystemGuiModule implements IModule {

  private final TargetsList targetsList;
  private final GuiTargets guiTargets;
  private final ClientWindowsModule windowsGuiModule;

  @Inject
  public SystemGuiModule(
      TargetsList targetsList,
      GuiTargets guiTargets,
      ClientWindowsModule windowsGuiModule
  ) {
    this.targetsList = targetsList;
    this.guiTargets = guiTargets;
    this.windowsGuiModule = windowsGuiModule;
  }

  @Override
  public void setUp() {
    EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void onGuiOpen(GuiOpenEvent event) {
    if (!(event.getGui() instanceof GuiChat)) {
      return;
    }

    GuiChat gui = (GuiChat) event.getGui();
    String defaultInputFieldText = ObfuscationReflectionHelper.getPrivateValue(
        GuiChat.class,
        gui,
        "field_146409_v"
    );
    event.setGui(new GuiSystem(
        defaultInputFieldText,
        this.guiTargets,
        this.targetsList,
        this.windowsGuiModule.getGuiWindows()
    ));
  }
}
