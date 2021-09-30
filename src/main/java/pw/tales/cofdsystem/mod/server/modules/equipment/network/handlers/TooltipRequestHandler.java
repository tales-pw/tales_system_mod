package pw.tales.cofdsystem.mod.server.modules.equipment.network.handlers;

import com.google.inject.Inject;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.common.modules.equipment.network.messages.TooltipRequestMessage;
import pw.tales.cofdsystem.mod.common.modules.equipment.network.messages.TooltipResponseMessage;
import pw.tales.cofdsystem.mod.server.modules.equipment.TooltipServerModule;

public class TooltipRequestHandler
    implements IMessageHandler<TooltipRequestMessage, IMessage> {

  @Inject
  private static TooltipServerModule tooltipServerModule;

  @Override
  public TooltipResponseMessage onMessage(TooltipRequestMessage message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().player;
    ItemStack itemStack = message.getItemStack();

    tooltipServerModule.buildTooltip(
        player,
        itemStack
    ).thenAccept(component -> {
      TooltipResponseMessage responseMessage = new TooltipResponseMessage(
          message.getRequestID(),
          component
      );

      TalesSystem.network.sendTo(responseMessage, player);
    });

    return null;
  }
}
