package pw.tales.cofdsystem.mod.server.errors.error_handlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.common.errors.handlers.BaseErrorHandler;
import pw.tales.cofdsystem.mod.server.modules.go_source.exceptions.GOFetchingException;
import pw.tales.cofdsystem.mod.server.modules.notification.NotificationModule;
import pw.tales.cofdsystem.mod.server.views.View;

@Singleton
public class GOFetchingErrorHandler extends BaseErrorHandler<GOFetchingException> {

  private final NotificationModule notificationModule;

  @Inject
  protected GOFetchingErrorHandler(NotificationModule notificationModule) {
    super(GOFetchingException.class);
    this.notificationModule = notificationModule;
  }

  @Override
  public void handleError(ICommandSender recipient, GOFetchingException e) {
    String dn = e.getDN();

    this.notificationModule.sendDirectly(recipient, new GoFetchingErrorView(dn));

    TalesSystem.logger.error(
        "Error while loading character {}: {}",
        dn,
        e.getMessage()
    );
  }

  static class GoFetchingErrorView extends View {

    private final String dn;

    public GoFetchingErrorView(String dn) {
      this.dn = dn;
    }

    @Override
    public ITextComponent build(EntityPlayerMP viewer) {
      ITextComponent component = new TextComponentTranslation(
          "command.gameobject.use.fetch.failure",
          viewer.getDisplayName(),
          this.dn
      );

      component.getStyle().setColor(TextFormatting.RED);
      return component;
    }
  }
}
