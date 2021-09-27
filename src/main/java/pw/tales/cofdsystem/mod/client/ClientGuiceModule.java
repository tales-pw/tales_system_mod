package pw.tales.cofdsystem.mod.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.mod.client.modules.equipment.network.handlers.TooltipResponseHandler;
import pw.tales.cofdsystem.mod.client.modules.gui_windows.network.handlers.SystemWindowRemoveHandler;
import pw.tales.cofdsystem.mod.client.modules.gui_windows.network.handlers.SystemWindowUpdateHandler;
import pw.tales.cofdsystem.mod.common.CommonGuiceModule;

public class ClientGuiceModule extends CommonGuiceModule {

  public ClientGuiceModule(CofDSystem cofdSystem) {
    super(cofdSystem);
  }

  @Override
  protected void configure() {
    super.configure();
    bind(Minecraft.class).toInstance(Minecraft.getMinecraft());
    bind(ClientCommandHandler.class).toInstance(ClientCommandHandler.instance);

    requestStaticInjection(SystemWindowUpdateHandler.class);
    requestStaticInjection(SystemWindowRemoveHandler.class);
    requestStaticInjection(TooltipResponseHandler.class);
  }
}
