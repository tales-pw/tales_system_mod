package pw.tales.cofdsystem.mod.server.modules.scene.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Set;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import pw.tales.cofdsystem.mod.common.modules.scene.command.SceneEndCommand;
import pw.tales.cofdsystem.mod.server.modules.gui_windows.WindowsModule;
import pw.tales.cofdsystem.mod.server.modules.operators.OperatorsModule;
import pw.tales.cofdsystem.mod.server.modules.scene.SceneModule;
import pw.tales.cofdsystem.scene.Scene;


@Singleton
public class ServerSceneEndCommand extends SceneEndCommand {

  private final OperatorsModule operatorsModule;
  private final SceneModule sceneModule;
  private final WindowsModule windowsModule;

  @Inject
  public ServerSceneEndCommand(
      OperatorsModule operatorsModule,
      SceneModule sceneModule,
      WindowsModule windowsModule
  ) {
    this.operatorsModule = operatorsModule;
    this.sceneModule = sceneModule;
    this.windowsModule = windowsModule;
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

    Scene scene = this.sceneModule.getBoundScene(entity);
    if (scene == null) {
      throw new CommandException("command.scene.menu.bound.no");
    }

    scene.end();

    // Unbind scene
    Set<ServerPlayerEntity> entities = this.sceneModule.unbindScene(scene);
    this.sceneModule.getSceneRegistry().unregister(scene);
    this.windowsModule.removeWindowForAll(scene.getDN());

    // Update scene menu for player controlling it
    entities.forEach(this.sceneModule::updateSceneMenuWindow);
  }
}
