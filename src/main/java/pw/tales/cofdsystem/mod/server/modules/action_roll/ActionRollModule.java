package pw.tales.cofdsystem.mod.server.modules.action_roll;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.action.events.roll.ActionPostRollEvent;
import pw.tales.cofdsystem.action.pool.ActionPool;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.common.IModule;
import pw.tales.cofdsystem.mod.common.haxe_adapters.HaxeFn;
import pw.tales.cofdsystem.mod.server.modules.action_roll.messages.TraitRollChatMessage;
import pw.tales.cofdsystem.mod.server.modules.notification.NotificationModule;
import pw.tales.cofdsystem.utils.events.HandlerPriority;

@Singleton
public class ActionRollModule implements IModule {

  private final CofDSystem system;
  private final NotificationModule notificationModule;

  @Inject
  public ActionRollModule(
      CofDSystem system,
      NotificationModule notificationModule
  ) {
    this.system = system;
    this.notificationModule = notificationModule;
  }

  public void setUp() {
    system.events.addHandler(
        ActionPostRollEvent.class,
        HaxeFn.wrap(this::onRollEvent),
        HandlerPriority.LOWEST
    );
  }

  public void onRollEvent(ActionPostRollEvent event) {
    ActionPool roll = event.getRoll();
    GameObject gameObject = roll.getGameObject();
    TraitRollChatMessage msg = new TraitRollChatMessage(event);
    this.notificationModule.sendGOAsOrigin(msg, gameObject);
  }
}
