package pw.tales.cofdsystem.mod.server.modules.notification;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.character.traits.advantages.health.events.GameObjectDamagedEvent;
import pw.tales.cofdsystem.character.traits.advantages.health.events.GameObjectDiedEvent;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.game_object.GameObjectState;
import pw.tales.cofdsystem.game_object.events.GameObjectEvent;
import pw.tales.cofdsystem.game_object.events.traits.TraitPostAttachEvent;
import pw.tales.cofdsystem.game_object.events.traits.TraitPostRemoveEvent;
import pw.tales.cofdsystem.mod.common.IModule;
import pw.tales.cofdsystem.mod.common.haxe_adapters.HaxeFn;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.GOEntityRelation;
import pw.tales.cofdsystem.mod.server.modules.gui_windows.WindowsModule;
import pw.tales.cofdsystem.mod.server.modules.notification.rpchat_messages.GameObjectDamagedMessage;
import pw.tales.cofdsystem.mod.server.modules.notification.rpchat_messages.GameObjectDiedMessage;
import pw.tales.cofdsystem.mod.server.modules.notification.rpchat_messages.TraitAddedMessage;
import pw.tales.cofdsystem.mod.server.modules.notification.rpchat_messages.TraitRemovedMessage;
import pw.tales.cofdsystem.mod.server.roleplaychat.DummySpeaker;
import pw.tales.cofdsystem.mod.server.roleplaychat.IMessage;
import pw.tales.cofdsystem.mod.server.views.View;
import pw.tales.cofdsystem.utils.events.HandlerPriority;
import ru.xunto.roleplaychat.RoleplayChatCore;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.features.middleware.distance.DistanceMiddleware;
import ru.xunto.roleplaychat.forge_1_12_2.ForgeEntitySpeaker;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Request;

/**
 * Module responsible for sending messages to GameObject's owner and etc.
 */
@Singleton
public class NotificationModule implements IModule {

  private final CofDSystem system;
  private final GOEntityRelation goEntityRelation;
  private final WindowsModule windowsModule;

  @Inject
  public NotificationModule(
      CofDSystem system,
      GOEntityRelation goEntityRelation,
      WindowsModule windowsModule
  ) {
    this.system = system;
    this.goEntityRelation = goEntityRelation;
    this.windowsModule = windowsModule;
  }

  @Override
  public void setUp() {
    this.register(
        TraitPostAttachEvent.class,
        TraitAddedMessage::new
    );
    this.register(
        TraitPostRemoveEvent.class,
        TraitRemovedMessage::new
    );
    this.register(
        GameObjectDamagedEvent.class,
        GameObjectDamagedMessage::new
    );
    this.register(
        GameObjectDiedEvent.class,
        GameObjectDiedMessage::new
    );
  }

  /**
   * Register event for notification.
   *
   * @param eventClass     Event class to notify about.
   * @param msgConstructor Function that returns IEventMessage.
   */
  public <T extends GameObjectEvent> void register(
      Class<T> eventClass,
      Function<T, IMessage> msgConstructor
  ) {
    this.system.events.addHandler(eventClass, HaxeFn.wrap(
        (T event) -> this.notifyEventGOAsOrigin(event, msgConstructor)
    ), HandlerPriority.LOWEST);
  }

  /**
   * Notifies about given event with message created with msgConstructor.
   * <p>
   * Uses GameObjects as point of origin for message sent with RoleplayChat.
   *
   * @param event          Event to notify about.
   * @param msgConstructor Object that creates IEventMessage.
   */
  public <T extends GameObjectEvent> void notifyEventGOAsOrigin(
      T event,
      Function<T, IMessage> msgConstructor
  ) {
    GameObject gameObject = event.getGameObject();

    if (gameObject.state != GameObjectState.ACTIVE) {
      return;
    }

    IMessage msg = msgConstructor.apply(event);
    this.sendGOAsOrigin(msg, gameObject);
  }

  /**
   * Send message with GameObject's entity or entities as origin.
   *
   * @param msg         RoleplayChat message.
   * @param gameObjects GameObjects
   */
  public void sendGOAsOrigin(
      IMessage msg,
      GameObject... gameObjects
  ) {
    Environment environment = msg.create();
    List<ISpeaker> origins = this.getOrigins(gameObjects);
    environment.getState().setValue(DistanceMiddleware.ORIGINS, origins);
    RoleplayChatCore.instance.process(new Request("", new DummySpeaker()), environment);
  }

  /**
   * Get speakers for GameObject.
   *
   * @param gameObjects array of game objects.
   * @return list of speaker.
   */
  private List<ISpeaker> getOrigins(GameObject... gameObjects) {
    return Arrays.stream(gameObjects)
        .map(this.goEntityRelation::getEntity)
        .map(ForgeEntitySpeaker::new)
        .collect(Collectors.toList());
  }

  /**
   * @param view       Window view builder.
   * @param windowDN   Unique window identifier.
   * @param gameObject GameObject to update window to.
   * @return Entities who received window update.
   */
  @Nullable
  public EntityPlayerMP updateGoWindow(
      View view,
      String windowDN,
      GameObject gameObject,
      boolean forcedUpdate
  ) {
    EntityPlayerMP entity = this.goEntityRelation.getEntity(
        gameObject,
        EntityPlayerMP.class
    );

    if (entity == null) {
      return null;
    }

    this.windowsModule.updateWindow(
        entity,
        view,
        windowDN,
        forcedUpdate
    );

    return entity;
  }

  /**
   * Send message directly to GameObject's entity or entities.
   *
   * @param view       View.
   * @param gameObject GameObject to send message to.
   * @return Entities who received message.
   */
  @Nullable
  public EntityPlayerMP sendGODirectly(View view, GameObject gameObject) {
    EntityPlayerMP entity = this.goEntityRelation.getEntity(
        gameObject,
        EntityPlayerMP.class
    );

    if (entity == null) {
      return null;
    }

    entity.sendMessage(view.build(entity));
    return entity;
  }

  /**
   * Send message directly to GameObject's entity or entities.
   *
   * @param component  Text Component.
   * @param gameObject GameObject to send message to.
   * @return Entities who received message.
   */
  @Nullable
  public EntityPlayerMP sendGODirectly(ITextComponent component, GameObject gameObject) {
    EntityPlayerMP entity = this.goEntityRelation.getEntity(
        gameObject,
        EntityPlayerMP.class
    );

    if (entity == null) {
      return null;
    }

    entity.sendMessage(component);
    return entity;
  }

  public void sendDirectly(ICommandSender recipient, View view) {
    if (!(recipient instanceof EntityPlayerMP)) {
      return;
    }

    EntityPlayerMP player = (EntityPlayerMP) recipient;
    player.sendMessage(view.build(player));
  }
}
