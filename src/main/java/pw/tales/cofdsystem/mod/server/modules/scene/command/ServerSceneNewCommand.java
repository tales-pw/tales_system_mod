package pw.tales.cofdsystem.mod.server.modules.scene.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.mod.common.modules.scene.command.SceneNewCommand;
import pw.tales.cofdsystem.mod.server.modules.operators.OperatorsModule;
import pw.tales.cofdsystem.mod.server.modules.scene.SceneModule;
import pw.tales.cofdsystem.scene.Scene;
import pw.tales.cofdsystem.utils.registry.Registry;


@Singleton
public class ServerSceneNewCommand extends SceneNewCommand {

  private final OperatorsModule operatorsModule;
  private final SceneModule sceneModule;
  private final CofDSystem cofdSystem;

  @Inject
  public ServerSceneNewCommand(
      OperatorsModule operatorsModule,
      SceneModule sceneModule,
      CofDSystem cofdSystem
  ) {
    this.operatorsModule = operatorsModule;
    this.sceneModule = sceneModule;
    this.cofdSystem = cofdSystem;
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

    Scene scene = Scene.create(this.cofdSystem);

    Registry<Scene> scenes = this.sceneModule.getSceneRegistry();
    scenes.register(scene);

    this.sceneModule.bindScene(entity, scene);
    this.sceneModule.updateSceneMenuWindow(entity);
  }
}
