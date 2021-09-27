package pw.tales.cofdsystem.mod.common.network;

import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class TalesMessageHandler<R extends IMessage> implements
    IMessageHandler<R, IMessage> {

  @Override
  public IMessage onMessage(R message, MessageContext ctx) {
    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    server.addScheduledTask(() -> {
      EntityPlayerMP player = ctx.getServerHandler().player;

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
        TextComponentTranslation component = new TextComponentTranslation(
            "commands.generic.exception");
        exception.printStackTrace();
        component.getStyle().setColor(TextFormatting.RED);
        player.sendMessage(component);
      }
    });

    return null;
  }

  public boolean checkPermission(MinecraftServer server, EntityPlayerMP player) {
    return true;
  }

  public abstract void process(EntityPlayerMP player, R message) throws CommandException;
}
