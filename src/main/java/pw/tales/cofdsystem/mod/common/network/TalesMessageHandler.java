package pw.tales.cofdsystem.mod.common.network;

import com.google.inject.Inject;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pw.tales.cofdsystem.mod.common.errors.IErrorHandler;

public abstract class TalesMessageHandler<R extends IMessage> implements
    IMessageHandler<R, IMessage> {

  @Inject
  public static IErrorHandler errors;

  @Override
  @Nullable
  public IMessage onMessage(R message, MessageContext ctx) {
    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    server.addScheduledTask(() -> {
      ServerPlayerEntity player = ctx.getServerHandler().player;

      if (!checkPermission(server, player)) {
        TextComponentTranslation component = new TextComponentTranslation(
            "commands.generic.permission"
        );
        component.getStyle().setColor(TextFormatting.RED);
        player.sendMessage(component);
      }

      try {
        this.process(player, message);
      } catch (CommandException e) {
        TextComponentTranslation component = new TextComponentTranslation(e.getMessage());
        component.getStyle().setColor(TextFormatting.RED);
        player.sendMessage(component);
      } catch (Exception exception) {
        this.handleErrors(player, exception);
      }
    });

    return null;
  }

  protected void handleErrors(
      ICommandSender sender,
      Throwable exception
  ) {
    errors.handle(sender, exception);
  }

  public boolean checkPermission(MinecraftServer server, ServerPlayerEntity player) {
    return true;
  }

  public abstract void process(ServerPlayerEntity player, R message) throws CommandException;
}
