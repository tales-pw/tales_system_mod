package pw.tales.cofdsystem.mod.server.modules.equipment.network.handlers;

import com.google.inject.Inject;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pw.tales.cofdsystem.mod.common.modules.equipment.network.messages.TooltipRequestMessage;
import pw.tales.cofdsystem.mod.common.modules.equipment.network.messages.TooltipResponseMessage;
import pw.tales.cofdsystem.mod.server.modules.equipment.TooltipServerModule;

public class TooltipRequestHandler
    implements IMessageHandler<TooltipRequestMessage, TooltipResponseMessage> {

  @Inject
  public static TooltipServerModule tooltipServerModule;

  @Override
  public TooltipResponseMessage onMessage(TooltipRequestMessage message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().player;
    ItemStack itemStack = message.getItemStack();

    ITextComponent tooltip = tooltipServerModule.buildTooltip(
        player,
        itemStack
    );

    return new TooltipResponseMessage(
        message.getRequestID(),
        tooltip
    );
  }
}
