package pw.tales.cofdsystem.mod.client.modules.go_relation_entity;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import java.util.Set;
import net.minecraft.command.CommandBase;
import net.minecraftforge.client.ClientCommandHandler;
import pw.tales.cofdsystem.mod.client.modules.ClientCommandModule;
import pw.tales.cofdsystem.mod.client.modules.go_relation_entity.command.ClientEntityGOBindCommand;
import pw.tales.cofdsystem.mod.client.modules.go_relation_entity.command.ClientEntityGOControlCommand;
import pw.tales.cofdsystem.mod.client.modules.go_relation_entity.command.ClientEntityGOUnloadCommand;

@Singleton
public class ClientGORelationModule extends ClientCommandModule {

  @Inject
  public ClientGORelationModule(
      ClientCommandHandler commandHandler,
      Injector injector
  ) {
    super(commandHandler, injector);
  }

  @Override
  public Set<Class<? extends CommandBase>> getCommandClasses() {
    return ImmutableSet.of(
        ClientEntityGOBindCommand.class,
        ClientEntityGOUnloadCommand.class,
        ClientEntityGOControlCommand.class
    );
  }
}
