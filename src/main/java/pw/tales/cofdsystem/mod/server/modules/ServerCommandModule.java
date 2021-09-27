package pw.tales.cofdsystem.mod.server.modules;

import com.google.inject.Injector;
import java.util.Set;
import net.minecraft.command.CommandBase;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import pw.tales.cofdsystem.mod.common.IModule;

public abstract class ServerCommandModule implements IModule {

  private final Injector injector;

  protected ServerCommandModule(Injector injector) {
    this.injector = injector;
  }

  @Override
  public void onServerStart(FMLServerStartingEvent event) {
    for (Class<? extends CommandBase> commandClass : this.getCommandClasses()) {
      CommandBase command = this.injector.getInstance(commandClass);
      event.registerServerCommand(command);
    }
  }

  public abstract Set<Class<? extends CommandBase>> getCommandClasses();
}
