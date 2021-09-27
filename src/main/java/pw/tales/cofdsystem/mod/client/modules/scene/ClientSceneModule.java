package pw.tales.cofdsystem.mod.client.modules.scene;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import java.util.Set;
import net.minecraft.command.CommandBase;
import net.minecraftforge.client.ClientCommandHandler;
import pw.tales.cofdsystem.mod.client.modules.ClientCommandModule;
import pw.tales.cofdsystem.mod.client.modules.scene.command.ClientSceneAddSelectedCommand;

@Singleton
public class ClientSceneModule extends ClientCommandModule {

  @Inject
  public ClientSceneModule(
      ClientCommandHandler commandHandler,
      Injector injector
  ) {
    super(commandHandler, injector);
  }

  @Override
  public Set<Class<? extends CommandBase>> getCommandClasses() {
    return ImmutableSet.of(
        ClientSceneAddSelectedCommand.class
    );
  }
}
