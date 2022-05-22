package pw.tales.cofdsystem.mod.server.modules.scene.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import pw.tales.cofdsystem.mod.common.modules.scene.command.SceneBindCommand;
import pw.tales.cofdsystem.mod.server.modules.operators.OperatorsModule;
import pw.tales.cofdsystem.mod.server.modules.scene.SceneModule;
import pw.tales.cofdsystem.scene.Scene;
import pw.tales.cofdsystem.utils.registry.Registry;


@Singleton
public class ServerSceneBindCommand extends SceneBindCommand {

  private final OperatorsModule operatorsModule;
  private final SceneModule sceneModule;

  @Inject
  public ServerSceneBindCommand(
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
    if (args.length < 1) {
      throw new CommandException("command.scene.bind.not_enough_arguments");
    }

    ServerPlayerEntity entity = getCommandSenderAsPlayer(sender);

    Registry<Scene> scenes = this.sceneModule.getSceneRegistry();
    Scene scene = scenes.getRecord(args[0]);

    if (scene == null) {
      throw new CommandException("command.scene.bind.scene_not_found", args[0]);
    }

    this.sceneModule.bindScene(entity, scene);
    this.sceneModule.updateSceneMenuWindow(entity);
  }
}
