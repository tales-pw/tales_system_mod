package pw.tales.cofdsystem.mod.client.modules.scene.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.client.modules.targets.TargetsList;
import pw.tales.cofdsystem.mod.common.modules.scene.command.SceneAddSelectedCommand;
import pw.tales.cofdsystem.mod.common.modules.scene.network.mesages.SceneAddMessage;


@Singleton
public class ClientSceneAddSelectedCommand extends SceneAddSelectedCommand {

  private final TargetsList targets;

  @Inject
  public ClientSceneAddSelectedCommand(TargetsList targets) {
    this.targets = targets;
  }

  @Override
  public void wrappedExecute(
      MinecraftServer server,
      ICommandSender sender,
      String[] args
  ) throws CommandException {
    TalesSystem.network.sendToServer(new SceneAddMessage(this.targets.getLoadedEntities()));
  }
}
