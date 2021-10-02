package pw.tales.cofdsystem.mod.server.modules.gui_windows;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.common.IModule;
import pw.tales.cofdsystem.mod.common.modules.gui_windows.network.messages.SystemWindowRemoveMessage;
import pw.tales.cofdsystem.mod.common.modules.gui_windows.network.messages.SystemWindowUpdateMessage;
import pw.tales.cofdsystem.mod.server.views.View;

/**
 * Module responsible for controlling client system windows.
 */
@Singleton
public class WindowsModule implements IModule {

  @Inject
  public WindowsModule() {
    // No dependency on other modules
  }

  /**
   * Open or update remote window.
   *
   * @param recipient Recipient.
   * @param view      View.
   * @param windowDN  Window identifier.
   * @param forceOpen Should window be forced to open.
   */
  public void updateWindow(
      Entity recipient,
      View view,
      String windowDN,
      boolean forceOpen
  ) {
    if (!(recipient instanceof EntityPlayerMP)) {
      return;
    }

    EntityPlayerMP player = (EntityPlayerMP) recipient;

    ITextComponent component = view.build(player);
    TalesSystem.network.sendTo(
        new SystemWindowUpdateMessage(windowDN, component, forceOpen),
        player
    );
  }

  /**
   * Remove (close) window for recipient.
   *
   * @param recipient Recipient.
   * @param windowDn  Window identifier.
   */
  public void removeWindow(Entity recipient, String windowDn) {
    if (!(recipient instanceof EntityPlayerMP)) {
      return;
    }

    TalesSystem.network.sendTo(
        new SystemWindowRemoveMessage(windowDn),
        (EntityPlayerMP) recipient
    );
  }

  /**
   * Remove (close) window for everybody.
   *
   * @param windowDn Window identifier.
   */
  public void removeWindowForAll(String windowDn) {
    TalesSystem.network.sendToAll(
        new SystemWindowRemoveMessage(windowDn)
    );
  }
}
