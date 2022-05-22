package pw.tales.cofdsystem.mod.server.errors.error_handlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import pw.tales.cofdsystem.mod.common.errors.handlers.BaseErrorHandler;
import pw.tales.cofdsystem.mod.server.modules.go_relation.exceptions.NotBoundException;
import pw.tales.cofdsystem.mod.server.modules.notification.NotificationModule;
import pw.tales.cofdsystem.mod.server.views.View;

@Singleton
public class NotBoundErrorHandler extends BaseErrorHandler<NotBoundException> {

  private final NotificationModule notificationModule;

  @Inject
  protected NotBoundErrorHandler(NotificationModule notificationModule) {
    super(NotBoundException.class);
    this.notificationModule = notificationModule;
  }

  @Override
  public void handleError(ICommandSender recipient, NotBoundException e) {
    Object holder = e.getHolder();
    this.notificationModule.sendDirectly(
        recipient,
        new NotBoundErrorView(holder)
    );
  }

  static class NotBoundErrorView extends View {

    private final Object holder;

    public NotBoundErrorView(Object holder) {
      this.holder = holder;
    }

    @Override
    public ITextComponent build(ServerPlayerEntity viewer) {
      String holderStr = this.getHolderString();

      ITextComponent components = new TextComponentTranslation(
          "chat.menu.attack.no_game_object",
          holderStr
      );

      return this.applyErrorStyle(components);
    }

    public String getHolderString() {
      if (this.holder instanceof PlayerEntity) {
        PlayerEntity player = (PlayerEntity) this.holder;
        return player.getDisplayNameString();
      } else {
        return this.holder.toString();
      }
    }
  }
}
