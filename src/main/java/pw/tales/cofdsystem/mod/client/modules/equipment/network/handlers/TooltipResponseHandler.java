package pw.tales.cofdsystem.mod.client.modules.equipment.network.handlers;

import com.google.inject.Inject;
import javax.annotation.Nullable;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pw.tales.cofdsystem.mod.client.modules.equipment.TooltipClientModule;
import pw.tales.cofdsystem.mod.common.modules.equipment.network.messages.TooltipResponseMessage;

public class TooltipResponseHandler implements IMessageHandler<TooltipResponseMessage, IMessage> {

  @Inject
  public static TooltipClientModule tooltipClientModule;

  @Override
  @Nullable
  public IMessage onMessage(TooltipResponseMessage message, MessageContext ctx) {
    ITextComponent tooltip = message.getTooltip();

    tooltipClientModule.handleTooltipResponse(message.getRequestID(), tooltip);
    return null;
  }
}
