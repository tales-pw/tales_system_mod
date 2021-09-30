package pw.tales.cofdsystem.mod.server.modules.attack;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.action.opposition.base.OppositionCompetitive;
import pw.tales.cofdsystem.action_attack.AttackAction;
import pw.tales.cofdsystem.action_attack.events.AttackInitiatedEvent;
import pw.tales.cofdsystem.action_attack.events.AttackMissEvent;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.common.IModule;
import pw.tales.cofdsystem.mod.common.haxe_adapters.HaxeFn;
import pw.tales.cofdsystem.mod.server.modules.attack.rpchat_messages.AttackInitiatedMessage;
import pw.tales.cofdsystem.mod.server.modules.attack.rpchat_messages.AttackMissMessage;
import pw.tales.cofdsystem.mod.server.modules.notification.NotificationModule;
import pw.tales.cofdsystem.utils.events.HandlerPriority;

@Singleton
public class AttackNotifications implements IModule {

  private final CofDSystem system;
  private final NotificationModule notificationModule;

  @Inject
  public AttackNotifications(
      CofDSystem system,
      NotificationModule notificationModule
  ) {
    this.system = system;
    this.notificationModule = notificationModule;
  }

  @Override
  public void setUp() {
    EVENT_BUS.register(this);

    this.system.events.addHandler(AttackMissEvent.class, HaxeFn.wrap(
        this::onAttackMissEvent
    ), HandlerPriority.LOWEST);

    this.system.events.addHandler(AttackInitiatedEvent.class, HaxeFn.wrap(
        this::onAttackInitiatedEvent
    ), HandlerPriority.LOWEST);
  }

  public void onAttackMissEvent(AttackMissEvent event) {
    AttackAction attackAction = event.getAttackAction();

    OppositionCompetitive opposition = attackAction.getCompetitiveOpposition();
    GameObject actor = opposition.getActorPool().getGameObject();
    GameObject target = opposition.getTargetPool().getGameObject();

    this.notificationModule.sendGOAsOrigin(
        new AttackMissMessage(actor),
        actor, target
    );
  }

  public void onAttackInitiatedEvent(AttackInitiatedEvent event) {
    AttackAction attackAction = event.getAttackAction();

    OppositionCompetitive opposition = attackAction.getCompetitiveOpposition();
    GameObject actor = opposition.getActorPool().getGameObject();
    GameObject target = opposition.getTargetPool().getGameObject();

    this.notificationModule.sendGOAsOrigin(
        new AttackInitiatedMessage(actor, target),
        actor, target
    );
  }
}
