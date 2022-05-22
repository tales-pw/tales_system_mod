package pw.tales.cofdsystem.mod.server.modules.simple_roll.network;

import com.google.inject.Inject;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.ServerPlayerEntity;
import pw.tales.cofdsystem.mod.common.network.TalesMessageHandler;
import pw.tales.cofdsystem.mod.common.network.messages.SimpleRollMessage;
import pw.tales.cofdsystem.mod.server.modules.simple_roll.SimpleRollModule;

public class SimpleRollMessageHandler extends TalesMessageHandler<SimpleRollMessage> {

  @SuppressWarnings("java:S1444")
  @Inject
  public static SimpleRollModule rollModule;

  @Override
  public void process(ServerPlayerEntity player, SimpleRollMessage message) throws CommandException {
    rollModule.roll(player, message.request);
  }
}
