package pw.tales.cofdsystem.mod.client.modules;

import com.google.inject.Injector;
import java.util.Set;
import net.minecraft.command.CommandBase;
import net.minecraftforge.client.ClientCommandHandler;
import pw.tales.cofdsystem.mod.common.IModule;

public abstract class ClientCommandModule implements IModule {

  private final ClientCommandHandler commandHandler;
  private final Injector injector;

  public ClientCommandModule(
      ClientCommandHandler commandHandler,
      Injector injector
  ) {

    this.commandHandler = commandHandler;
    this.injector = injector;
  }

  @Override
  public void setUp() {
    for (Class<? extends CommandBase> commandClass : this.getCommandClasses()) {
      CommandBase command = this.injector.getInstance(commandClass);
      commandHandler.registerCommand(command);
    }
  }

  public abstract Set<Class<? extends CommandBase>> getCommandClasses();
}
