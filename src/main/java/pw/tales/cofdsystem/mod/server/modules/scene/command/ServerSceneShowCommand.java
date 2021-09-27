package pw.tales.cofdsystem.mod.server.modules.scene.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import pw.tales.cofdsystem.mod.common.modules.scene.command.SceneShowCommand;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.GOEntityRelation;
import pw.tales.cofdsystem.mod.server.modules.scene.SceneModule;
import pw.tales.cofdsystem.scene.Scene;


@Singleton
public class ServerSceneShowCommand extends SceneShowCommand {

  private final SceneModule sceneModule;
  private final GOEntityRelation goEntityRelation;

  @Inject
  public ServerSceneShowCommand(
      SceneModule sceneModule,
      GOEntityRelation goEntityRelation
  ) {
    this.sceneModule = sceneModule;
    this.goEntityRelation = goEntityRelation;
  }

  @Override
  public void wrappedExecute(
      MinecraftServer server,
      ICommandSender sender,
      String[] args
  ) throws CommandException {
    EntityPlayerMP entity = getCommandSenderAsPlayer(sender);

    Scene scene = this.sceneModule.getBoundScene(entity);
    if (scene == null) {
      throw new CommandException("command.scene.menu.bound.no");
    }

    this.goEntityRelation.getGameObject(entity).thenAccept(
        gameObject -> this.sceneModule.updateTurnWindow(
            scene,
            gameObject,
            true
        )
    );
  }
}
