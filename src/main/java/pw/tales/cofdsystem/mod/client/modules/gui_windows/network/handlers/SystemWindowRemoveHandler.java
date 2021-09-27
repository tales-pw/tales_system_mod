package pw.tales.cofdsystem.mod.client.modules.gui_windows.network.handlers;

import com.google.inject.Inject;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pw.tales.cofdsystem.mod.client.modules.gui_windows.ClientWindowsModule;
import pw.tales.cofdsystem.mod.common.modules.gui_windows.network.messages.SystemWindowRemoveMessage;

public class SystemWindowRemoveHandler implements
    IMessageHandler<SystemWindowRemoveMessage, IMessage> {

  @Inject
  public static Minecraft mc;

  @Inject
  public static ClientWindowsModule module;

  @Override
  public IMessage onMessage(SystemWindowRemoveMessage message, MessageContext ctx) {
    if (message.isValid()) {
      mc.addScheduledTask(() -> module.remove(message.getId()));
    }

    return null;
  }
}
