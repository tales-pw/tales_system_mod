package pw.tales.cofdsystem.mod.server.modules.scene.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import pw.tales.cofdsystem.mod.common.modules.scene.command.SceneJoinCommand;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.GOEntityRelation;
import pw.tales.cofdsystem.mod.server.modules.operators.OperatorsModule;
import pw.tales.cofdsystem.mod.server.modules.scene.SceneModule;
import pw.tales.cofdsystem.scene.Scene;


@Singleton
public class ServerSceneJoinCommand extends SceneJoinCommand {

  private final OperatorsModule operatorsModule;
  private final SceneModule sceneModule;
  private final GOEntityRelation goEntityRelation;

  @Inject
  public ServerSceneJoinCommand(
      OperatorsModule operatorsModule,
      SceneModule sceneModule,
      GOEntityRelation goEntityRelation
  ) {
    this.operatorsModule = operatorsModule;
    this.sceneModule = sceneModule;
    this.goEntityRelation = goEntityRelation;
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
    ServerPlayerEntity player = getCommandSenderAsPlayer(sender);

    Scene scene = this.sceneModule.getBoundScene(player);
    if (scene == null) {
      throw new CommandException("command.scene.menu.bound.no");
    }

    this.goEntityRelation
        .getGameObject(player)
        .thenApply(gameObject -> {
          scene.getInitiative().add(gameObject);
          player.sendMessage(
              new TextComponentTranslation("command.scene.join.success")
          );
          return gameObject;
        })
        .exceptionally(e -> {
          this.handleErrors(player, e);
          return null;
        });
  }
}
