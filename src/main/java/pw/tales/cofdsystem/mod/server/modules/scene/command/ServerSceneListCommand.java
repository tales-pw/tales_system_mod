package pw.tales.cofdsystem.mod.server.modules.scene.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import pw.tales.cofdsystem.mod.common.modules.scene.command.SceneListCommand;
import pw.tales.cofdsystem.mod.server.modules.operators.OperatorsModule;
import pw.tales.cofdsystem.mod.server.modules.scene.SceneModule;
import pw.tales.cofdsystem.mod.server.modules.scene.views.SceneListView;


@Singleton
public class ServerSceneListCommand extends SceneListCommand {

  private final OperatorsModule operatorsModule;
  private final SceneModule sceneModule;

  @Inject
  public ServerSceneListCommand(
      OperatorsModule operatorsModule,
      SceneModule sceneModule
  ) {
    this.operatorsModule = operatorsModule;
    this.sceneModule = sceneModule;
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return this.operatorsModule.isOperator(sender);
  }

  @Override
  public void wrappedExecute(
      MinecraftServer server,
      ICommandSender sender,
      String[] args
  ) throws CommandException {
    ServerPlayerEntity entity = getCommandSenderAsPlayer(sender);
    SceneListView view = new SceneListView(sceneModule);
    this.sceneModule.updateSceneWindow(view, entity, false);
  }
}
