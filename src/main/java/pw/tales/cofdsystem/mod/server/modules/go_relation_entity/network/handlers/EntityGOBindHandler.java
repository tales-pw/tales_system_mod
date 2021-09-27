package pw.tales.cofdsystem.mod.server.modules.go_relation_entity.network.handlers;

import com.google.inject.Inject;
import java.util.concurrent.CompletableFuture;
import net.minecraft.command.CommandException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.permission.PermissionAPI;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.Utils;
import pw.tales.cofdsystem.mod.common.modules.go_relation_entity.network.messages.EntityGOBindMessage;
import pw.tales.cofdsystem.mod.common.network.TalesMessageHandler;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.GOEntityRelation;
import pw.tales.cofdsystem.mod.server.modules.operators.OperatorsModule;

public class EntityGOBindHandler extends TalesMessageHandler<EntityGOBindMessage> {

  @SuppressWarnings("java:S1444")
  @Inject
  public static GOEntityRelation goEntityRelation;

  @Override
  public boolean checkPermission(MinecraftServer server, EntityPlayerMP player) {
    return PermissionAPI.hasPermission(
        player,
        OperatorsModule.SYSTEM_OPERATOR_PERMISSION
    );
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
    CompletableFuture<GameObject> future;
    if (dn.equals("")) {
      future = CompletableFuture.completedFuture(null);
    } else {
      future = goEntityRelation.getGameObject(dn);
    }

    // Notify command is working
    TextComponentTranslation startMsg = new TextComponentTranslation(
        "command.gameobject.use.fetch.attempt",
        finalTarget.getDisplayName(),
        dn
    );
    startMsg.getStyle().setColor(TextFormatting.GREEN);
    player.sendMessage(startMsg);

    Utils.merge(
        goEntityRelation.getGameObject(finalTarget).exceptionally(e -> null),
        future
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

      // Notify about success
      TextComponentTranslation successMsg = new TextComponentTranslation(
          "command.gameobject.use.fetch.success",
          finalTarget.getDisplayName(),
          newGO
      );
      successMsg.getStyle().setColor(TextFormatting.GREEN);
      player.sendMessage(successMsg);

      return newGO;
    }).exceptionally(e -> {
      TextComponentTranslation errorMsg = new TextComponentTranslation(
          "command.gameobject.use.fetch.failure",
          finalTarget.getDisplayName(),
          dn
      );
      errorMsg.getStyle().setColor(TextFormatting.RED);
      player.sendMessage(errorMsg);

      TalesSystem.logger.error("Error while loading character {}: {}", dn, e.getMessage());
      return null;
    });
  }
}
