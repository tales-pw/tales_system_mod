package pw.tales.cofdsystem.mod.client.modules.gui_windows.network.handlers;

import com.google.inject.Inject;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pw.tales.cofdsystem.mod.client.modules.gui_windows.ClientWindowsModule;
import pw.tales.cofdsystem.mod.common.modules.gui_windows.network.messages.SystemWindowUpdateMessage;

public class SystemWindowUpdateHandler implements
    IMessageHandler<SystemWindowUpdateMessage, IMessage> {

  @Inject
  public static Minecraft mc;

  @Inject
  public static ClientWindowsModule module;

  @Override
  public IMessage onMessage(SystemWindowUpdateMessage message, MessageContext ctx) {
    if (message.isValid()) {
      mc.addScheduledTask(() -> module.update(
          message.getId(),
          message.getComponent(),
          message.getForceOpen()
      ));
    }

    return null;
  }
}
