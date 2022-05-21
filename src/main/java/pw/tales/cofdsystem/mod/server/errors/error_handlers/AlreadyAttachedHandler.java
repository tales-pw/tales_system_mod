package pw.tales.cofdsystem.mod.server.errors.error_handlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.common.errors.handlers.BaseErrorHandler;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.exceptions.AlreadyAttachedException;
import pw.tales.cofdsystem.mod.server.modules.notification.NotificationModule;
import pw.tales.cofdsystem.mod.server.views.View;

@Singleton
public class AlreadyAttachedHandler extends BaseErrorHandler<AlreadyAttachedException> {
  private final NotificationModule notificationModule;

  @Inject
  protected AlreadyAttachedHandler(NotificationModule notificationModule) {
    super(AlreadyAttachedException.class);
    this.notificationModule = notificationModule;
  }

  @Override
  public void handleError(ICommandSender recipient, AlreadyAttachedException e) {
    Entity targetEntity = e.getTargetEntity();
    Entity attachedEntity = e.getAttachedEntity();
    GameObject gameObject = e.getGameObject();

    this.notificationModule.sendDirectly(recipient, new AlreadyAttachedView(
        targetEntity,
        gameObject,
        attachedEntity
    ));

    TalesSystem.logger.warn(
        "Attempt to attach {} to {}, which is already attached to {}.",
        targetEntity,
        gameObject,
        attachedEntity
    );
  }

  static class AlreadyAttachedView extends View {

    private final Entity targetEntity;
    private final GameObject gameObject;
    private final Entity attachedEntity;

    public AlreadyAttachedView(Entity targetEntity, GameObject gameObject, Entity attachedEntity) {
      this.targetEntity = targetEntity;
      this.gameObject = gameObject;
      this.attachedEntity = attachedEntity;
    }

    @Override
    public ITextComponent build(EntityPlayerMP viewer) {
      ITextComponent component = new TextComponentTranslation(
          "gameobject.attach.failure",
          this.targetEntity.getDisplayName(),
          this.gameObject.getDN(),
          this.attachedEntity.getName()
      );
      component.getStyle().setColor(TextFormatting.RED);
      return component;
    }
  }
}
