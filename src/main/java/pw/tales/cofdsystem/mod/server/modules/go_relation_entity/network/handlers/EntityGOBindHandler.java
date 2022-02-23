package pw.tales.cofdsystem.mod.server.modules.go_relation_entity.network.handlers;

import com.google.inject.Inject;
import java.util.concurrent.CompletableFuture;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.permission.PermissionAPI;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.Utils;
import pw.tales.cofdsystem.mod.common.modules.go_relation_entity.network.messages.EntityGOBindMessage;
import pw.tales.cofdsystem.mod.common.network.TalesMessageHandler;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.GOEntityRelation;
import pw.tales.cofdsystem.mod.server.modules.go_source_local.LocalGOModule;
import pw.tales.cofdsystem.mod.server.modules.operators.OperatorsModule;

public class EntityGOBindHandler extends TalesMessageHandler<EntityGOBindMessage> {

  @SuppressWarnings("java:S1444")
  @Inject
  public static GOEntityRelation goEntityRelation;

  @SuppressWarnings("java:S1444")
  @Inject
  public static LocalGOModule localStorageModule;

  @Override
  public boolean checkPermission(MinecraftServer server, EntityPlayerMP player) {
    return PermissionAPI.hasPermission(
        player,
        OperatorsModule.SYSTEM_OPERATOR_PERMISSION
    );
  }

  private void notifyUseClone(EntityPlayerMP sender, GameObject go, GameObject clone) {
    // Notify command is working
    TextComponentTranslation startMsg = new TextComponentTranslation(
        "command.gameobject.use.fetch.clone",
        clone,
        go
    );
    startMsg.getStyle().setColor(TextFormatting.GREEN);
    sender.sendMessage(startMsg);
  }

  public void notifyStart(ICommandSender sender, Entity target, String dn) {
    // Notify command is working
    TextComponentTranslation startMsg = new TextComponentTranslation(
        "command.gameobject.use.fetch.attempt",
        target.getDisplayName(),
        dn
    );
    startMsg.getStyle().setColor(TextFormatting.GREEN);
    sender.sendMessage(startMsg);
  }

  public void notifySuccess(ICommandSender sender, Entity target, GameObject gameObject) {
    TextComponentTranslation successMsg = new TextComponentTranslation(
        "command.gameobject.use.fetch.success",
        target.getDisplayName(),
        gameObject
    );
    successMsg.getStyle().setColor(TextFormatting.GREEN);
    sender.sendMessage(successMsg);
  }

  @Override
  public void process(EntityPlayerMP player, EntityGOBindMessage message) throws CommandException {
    // Who to bind
    Entity[] targets = message.getEntities();

    if (targets.length > 1) {
      throw new CommandException("command.gameobject.use.failure.multiple");
    }

    Entity target = player;
    if (targets.length > 0) {
      target = targets[0];
    }
    final Entity finalTarget = target;

    // To whom to bind
    String dn = message.getDn();
    CompletableFuture<GameObject> gameObjectFuture;

    if (dn.equals("")) {
      gameObjectFuture = CompletableFuture.completedFuture(null);
    } else {
      gameObjectFuture = goEntityRelation.getGameObject(dn);
    }

    // Notify command is working
    this.notifyStart(player, finalTarget, dn);

    // Use game object's clone
    if (message.isClone()) {
      gameObjectFuture = gameObjectFuture.thenApply(go -> {
        GameObject clone = localStorageModule.cloneGameObject(go);
        this.notifyUseClone(player, go, clone);
        return clone;
      });
    }

    Utils.merge(
        goEntityRelation.getGameObject(finalTarget).exceptionally(e -> null),
        gameObjectFuture
    ).thenApply(entry -> {
      // Unbind old
      GameObject oldGO = entry.getLeft();
      if (oldGO != null) {
        goEntityRelation.bind(finalTarget, null);
        goEntityRelation.detach(finalTarget, oldGO);
      }

      // Bind new
      GameObject newGO = entry.getRight();
      goEntityRelation.bind(finalTarget, newGO);
      if (newGO != null) {
        goEntityRelation.attach(finalTarget, newGO);
      }

      this.notifySuccess(player, finalTarget, newGO);

      return newGO;
    }).exceptionally(e -> {
      this.handleErrors(player, e);
      return null;
    });
  }
}
