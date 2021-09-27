package pw.tales.cofdsystem.mod.server.modules.simple_roll.network;

import com.google.inject.Inject;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pw.tales.cofdsystem.mod.common.network.messages.SimpleRollMessage;
import pw.tales.cofdsystem.mod.server.modules.simple_roll.SimpleRollModule;

public class SimpleRollMessageHandler implements IMessageHandler<SimpleRollMessage, IMessage> {

  @SuppressWarnings("java:S1444")
  @Inject
  public static SimpleRollModule rollModule;

  @Override
  public IMessage onMessage(SimpleRollMessage message, MessageContext ctx) {
    rollModule.roll(ctx.getServerHandler().player, message.request);
    return null;
  }
}
