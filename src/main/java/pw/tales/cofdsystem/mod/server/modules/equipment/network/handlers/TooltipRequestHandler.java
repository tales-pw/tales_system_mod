package pw.tales.cofdsystem.mod.server.modules.equipment.network.handlers;

import com.google.inject.Inject;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.common.modules.equipment.network.messages.TooltipRequestMessage;
import pw.tales.cofdsystem.mod.common.modules.equipment.network.messages.TooltipResponseMessage;
import pw.tales.cofdsystem.mod.common.network.TalesMessageHandler;
import pw.tales.cofdsystem.mod.server.modules.equipment.TooltipServerModule;

public class TooltipRequestHandler extends TalesMessageHandler<TooltipRequestMessage> {

  @Inject
  private static TooltipServerModule tooltipServerModule;

  @Override
  public void process(ServerPlayerEntity player, TooltipRequestMessage message)
      throws CommandException {
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
    }).exceptionally(e -> {
      this.handleErrors(player, e);
      return null;
    });
  }
}
