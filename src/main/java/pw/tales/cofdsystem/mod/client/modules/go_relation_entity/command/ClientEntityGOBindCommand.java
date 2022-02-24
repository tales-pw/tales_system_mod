package pw.tales.cofdsystem.mod.client.modules.go_relation_entity.command;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.client.modules.targets.TargetsList;
import pw.tales.cofdsystem.mod.common.modules.go_relation_entity.command.EntityGOBindCommand;
import pw.tales.cofdsystem.mod.common.modules.go_relation_entity.network.messages.EntityGOBindMessage;

@Singleton
public class ClientEntityGOBindCommand extends EntityGOBindCommand {

  private final TargetsList targets;

  @Inject
  public ClientEntityGOBindCommand(TargetsList targets) {
    this.targets = targets;
  }

  @Override
  public void wrappedExecute(
      MinecraftServer server,
      ICommandSender sender,
      String[] arrayArgs
  ) throws CommandException {
    ArrayList<String> args = Lists.newArrayList(arrayArgs);

    boolean clone = false;
    if (args.contains("-c")) {
      clone = true;
      args.remove("-c");
    }

    String dn = "";
    if (!args.isEmpty()) {
      dn = args.remove(0);
    }

    TalesSystem.network.sendToServer(
        new EntityGOBindMessage(dn, clone, this.targets.getLoadedEntities())
    );
  }
}
