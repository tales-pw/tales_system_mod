package pw.tales.cofdsystem.mod.client.modules.go_relation_entity.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.client.modules.targets.TargetsList;
import pw.tales.cofdsystem.mod.common.modules.go_relation_entity.command.EntityGOControlCommand;
import pw.tales.cofdsystem.mod.common.modules.go_relation_entity.network.messages.EntityGOControlMessage;

@Singleton
public class ClientEntityGOControlCommand extends EntityGOControlCommand {

  private final TargetsList targets;

  @Inject
  public ClientEntityGOControlCommand(TargetsList targets) {
    this.targets = targets;
  }

  @Override
  public void wrappedExecute(
      MinecraftServer server,
      ICommandSender sender,
      String[] args
  ) throws CommandException {
    TalesSystem.network.sendToServer(
        new EntityGOControlMessage(this.targets.getEntities())
    );
  }
}
