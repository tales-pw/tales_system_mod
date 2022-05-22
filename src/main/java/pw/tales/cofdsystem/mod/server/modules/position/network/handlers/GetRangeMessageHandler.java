package pw.tales.cofdsystem.mod.server.modules.position.network.handlers;

import com.google.inject.Inject;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import pw.tales.cofdsystem.common.EnumRange;
import pw.tales.cofdsystem.mod.common.modules.position.network.messages.GetRangeMessage;
import pw.tales.cofdsystem.mod.common.network.TalesMessageHandler;
import pw.tales.cofdsystem.mod.server.modules.position.ServerPositionModule;
import pw.tales.cofdsystem.mod.server.modules.position.view.RangeView;

public class GetRangeMessageHandler extends TalesMessageHandler<GetRangeMessage> {

  @Inject
  private static ServerPositionModule positionModule;

  @Override
  public void process(ServerPlayerEntity player, GetRangeMessage message) throws CommandException {
    Entity[] entities = message.getEntities();

    ICommandSender from;
    ICommandSender to;

    if (entities.length == 1) {
      from = player;
      to = entities[0];
    } else if (entities.length == 2) {
      from = entities[0];
      to = entities[1];
    } else {
      throw new WrongUsageException("command.range.wrong_usage");
    }

    if (from == null) {
      throw new WrongUsageException("command.range.from_unloaded");
    }

    if (to == null) {
      throw new WrongUsageException("command.range.to_unloaded");
    }

    double distance = positionModule.getDistance(from, to);
    EnumRange range = positionModule.measureDistance(distance);

    RangeView rangeView = new RangeView(
        from, to, distance, range
    );
    player.sendMessage(rangeView.build(player));
  }
}
