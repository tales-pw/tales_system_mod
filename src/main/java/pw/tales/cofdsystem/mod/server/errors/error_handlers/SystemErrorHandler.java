package pw.tales.cofdsystem.mod.server.errors.error_handlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import pw.tales.cofdsystem.exceptions.CofDSystemException;
import pw.tales.cofdsystem.mod.common.errors.handlers.BaseErrorHandler;
import pw.tales.cofdsystem.mod.server.modules.notification.NotificationModule;
import pw.tales.cofdsystem.mod.server.views.View;

@Singleton
public class SystemErrorHandler extends BaseErrorHandler<CofDSystemException> {

  private final NotificationModule notificationModule;

  @Inject
  public SystemErrorHandler(NotificationModule notificationModule) {
    super(CofDSystemException.class);
    this.notificationModule = notificationModule;
  }

  @Override
  public void handleError(ICommandSender recipient, CofDSystemException e) {
    e.printStackTrace();
    this.notificationModule.sendDirectly(
        recipient,
        new SystemErrorView(e)
    );
  }

  static class SystemErrorView extends View {

    private final CofDSystemException e;

    public SystemErrorView(CofDSystemException e) {
      this.e = e;
    }

    @Override
    public ITextComponent build(EntityPlayerMP viewer) {
      ITextComponent component = new TextComponentTranslation(
          "command.system.error.unknown_system_error",
          e.getMessage()
      );
      return this.applyErrorStyle(component);
    }
  }
}
