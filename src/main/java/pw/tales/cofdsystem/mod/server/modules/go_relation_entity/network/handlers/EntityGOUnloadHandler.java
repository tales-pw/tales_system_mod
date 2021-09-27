package pw.tales.cofdsystem.mod.server.modules.go_relation_entity.network.handlers;

import com.google.inject.Inject;
import net.minecraft.command.CommandException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.server.permission.PermissionAPI;
import pw.tales.cofdsystem.mod.common.modules.go_relation_entity.network.messages.EntityGOUnloadMessage;
import pw.tales.cofdsystem.mod.common.network.TalesMessageHandler;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.GOEntityRelation;
import pw.tales.cofdsystem.mod.server.modules.operators.OperatorsModule;

public class EntityGOUnloadHandler extends TalesMessageHandler<EntityGOUnloadMessage> {

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
  public void process(
      EntityPlayerMP player,
      EntityGOUnloadMessage message
  ) throws CommandException {
    Entity[] targets = message.getEntities();

    if (targets.length > 1) {
      throw new CommandException("command.gameobject.use.failure.multiple");
    }

    Entity target = player;
    if (targets.length > 0) {
      target = targets[0];
    }

    final Entity finalTarget = target;
    goEntityRelation.unload(finalTarget);

    player.sendMessage(
        new TextComponentTranslation(
            "command.gameobject.unload",
            finalTarget.getDisplayName()
        )
    );
  }
}
