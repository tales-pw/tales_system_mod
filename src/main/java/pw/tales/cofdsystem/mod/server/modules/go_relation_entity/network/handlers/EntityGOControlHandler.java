package pw.tales.cofdsystem.mod.server.modules.go_relation_entity.network.handlers;

import com.google.inject.Inject;
import net.minecraft.command.CommandException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.permission.PermissionAPI;
import pw.tales.cofdsystem.mod.common.modules.go_relation_entity.network.messages.EntityGOControlMessage;
import pw.tales.cofdsystem.mod.common.network.TalesMessageHandler;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.GOEntityRelation;
import pw.tales.cofdsystem.mod.server.modules.operators.OperatorsModule;

public class EntityGOControlHandler extends TalesMessageHandler<EntityGOControlMessage> {

  @SuppressWarnings("java:S1444")
  @Inject
  public static GOEntityRelation goEntityRelation;

  @Override
  public boolean checkPermission(MinecraftServer server, ServerPlayerEntity player) {
    return PermissionAPI.hasPermission(
        player,
        OperatorsModule.SYSTEM_OPERATOR_PERMISSION
    );
  }

  @Override
  public void process(ServerPlayerEntity player, EntityGOControlMessage message)
      throws CommandException {
    Entity[] targets = message.getEntities();

    if (targets.length > 1) {
      throw new CommandException("command.gameobject.use.failure.multiple");
    }

    Entity target = null;
    if (targets.length > 0) {
      target = targets[0];
    }

    goEntityRelation.control(player, target);

    TextComponentTranslation msg = new TextComponentTranslation(
        "command.gameobject.control",
        target
    );
    msg.getStyle().setColor(TextFormatting.GREEN);
    player.sendMessage(msg);
  }
}
