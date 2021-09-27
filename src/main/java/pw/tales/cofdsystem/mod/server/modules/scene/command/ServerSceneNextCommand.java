package pw.tales.cofdsystem.mod.server.modules.scene.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.common.modules.scene.command.SceneNextCommand;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.GOEntityRelation;
import pw.tales.cofdsystem.mod.server.modules.operators.OperatorsModule;
import pw.tales.cofdsystem.mod.server.modules.scene.SceneModule;
import pw.tales.cofdsystem.scene.Scene;
import pw.tales.cofdsystem.scene.turns.Turns;


@Singleton
public class ServerSceneNextCommand extends SceneNextCommand {

  private final OperatorsModule operatorsModule;
  private final SceneModule initiativeModule;
  private final GOEntityRelation goEntityRelation;

  @Inject
  public ServerSceneNextCommand(
      OperatorsModule operatorsModule,
      SceneModule sceneModule,
      GOEntityRelation goEntityRelation
  ) {
    this.operatorsModule = operatorsModule;
    this.initiativeModule = sceneModule;
    this.goEntityRelation = goEntityRelation;
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return true;
  }

  @Override
  public void wrappedExecute(
      MinecraftServer server,
      ICommandSender sender,
      String[] args
  ) throws CommandException {
    EntityPlayerMP entity = getCommandSenderAsPlayer(sender);

    if (args.length < 1) {
      throw new CommandException("command.attack.show.bind.not_enough_arguments");
    }

    String sceneDn = args[0];
    Scene scene = this.initiativeModule.getSceneRegistry().getRecord(sceneDn);

    if (scene == null) {
      throw new CommandException(
          "command.scene.next.no_scene",
          sceneDn
      );
    }

    this.goEntityRelation.getGameObject(entity).thenAccept(gameObject -> {
      Turns turns = scene.getTurns();

      if (!this.canNext(entity, gameObject, turns)) {
        this.sendError(sender, new CommandException("scene.initiative.next.not_allowed"));
        return;
      }

      turns.nextTurn();
    });
  }

  public boolean canNext(EntityPlayer entity, GameObject gameObject, Turns turns) {
    boolean isSenderTurn = turns.getTurn() == gameObject;
    boolean isSenderOp = this.operatorsModule.isOperator(entity);
    return isSenderOp || isSenderTurn;
  }
}
