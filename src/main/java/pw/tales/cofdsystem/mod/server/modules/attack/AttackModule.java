package pw.tales.cofdsystem.mod.server.modules.attack;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.action.opposition.base.OppositionCompetitive;
import pw.tales.cofdsystem.action_attack.AttackAction;
import pw.tales.cofdsystem.action_attack.builder.AttackBuilder;
import pw.tales.cofdsystem.action_attack.events.AttackInitiatedEvent;
import pw.tales.cofdsystem.action_attack.events.AttackMissEvent;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.Utils;
import pw.tales.cofdsystem.mod.common.haxe_adapters.HaxeFn;
import pw.tales.cofdsystem.mod.server.modules.ServerCommandModule;
import pw.tales.cofdsystem.mod.server.modules.attack.command.AttackShowCommand;
import pw.tales.cofdsystem.mod.server.modules.attack.command.ConfigureActorCommand;
import pw.tales.cofdsystem.mod.server.modules.attack.command.ConfigureTargetCommand;
import pw.tales.cofdsystem.mod.server.modules.attack.rpchat_messages.AttackInitiatedMessage;
import pw.tales.cofdsystem.mod.server.modules.attack.rpchat_messages.AttackMissMessage;
import pw.tales.cofdsystem.mod.server.modules.attack.views.ActorMenuView;
import pw.tales.cofdsystem.mod.server.modules.attack.views.NoGameObjectView;
import pw.tales.cofdsystem.mod.server.modules.attack.views.TargetMenuView;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.GOEntityRelation;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.exceptions.EntityNotBoundException;
import pw.tales.cofdsystem.mod.server.modules.go_source.events.GameObjectUnloadingEvent;
import pw.tales.cofdsystem.mod.server.modules.notification.NotificationModule;
import pw.tales.cofdsystem.mod.server.modules.operators.OperatorsModule;
import pw.tales.cofdsystem.utils.events.HandlerPriority;

@Singleton
public class AttackModule extends ServerCommandModule {

  public final AttackManager attackManager;
  private final CofDSystem system;
  private final GOEntityRelation entityRelation;
  private final OperatorsModule operators;
  private final NotificationModule notificationModule;

  @Inject
  public AttackModule(
      CofDSystem system,
      GOEntityRelation entityRelation,
      OperatorsModule operatorsModule,
      NotificationModule notificationModule,
      AttackManager attackManager,
      Injector injector
  ) {
    super(injector);
    this.attackManager = attackManager;
    this.system = system;
    this.entityRelation = entityRelation;
    this.operators = operatorsModule;
    this.notificationModule = notificationModule;
  }

  @Override
  public void setUp() {
    EVENT_BUS.register(this);

    system.events.addHandler(AttackMissEvent.class, HaxeFn.wrap(
        this::onAttackMissEvent
    ), HandlerPriority.LOWEST);

    system.events.addHandler(AttackInitiatedEvent.class, HaxeFn.wrap(
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

  @SubscribeEvent
  public void onGameObjectUnloading(GameObjectUnloadingEvent.PRE event) {
    this.attackManager.clearGOAttacks(event.getGameObject());
  }

  public CompletableFuture<UUID> attack(Entity mcAttacker, Entity mcTarget) {
    return Utils.merge(
        this.entityRelation.getGameObject(mcAttacker, false),
        this.entityRelation.getGameObject(mcTarget)
    ).thenApply(pair -> {
      GameObject actor = pair.getLeft();
      GameObject target = pair.getRight();

      AttackBuilder builder = new AttackBuilder(actor, target);

      UUID uuid = attackManager.save(builder);
      String windowDn = uuid.toString();

      this.notificationModule.updateGoWindow(
          new ActorMenuView(uuid, this.attackManager, builder),
          windowDn,
          actor,
          true
      );

      this.notificationModule.updateGoWindow(
          new TargetMenuView(uuid, this.attackManager, builder),
          windowDn,
          target,
          true
      );

      this.operators.showWindow(
          new ActorMenuView(uuid, this.attackManager, builder),
          windowDn
      );

      return uuid;
    }).exceptionally(origException -> {

      if (origException instanceof EntityNotBoundException) {
        this.handleNoGameObject(
            mcAttacker,
            (EntityNotBoundException) origException
        );
      } else {
        origException.printStackTrace();
      }

      return null;
    });
  }

  private void handleNoGameObject(Entity recipient, EntityNotBoundException e) {
    if (!(recipient instanceof EntityPlayerMP)) {
      return;
    }

    EntityPlayerMP player = (EntityPlayerMP) recipient;
    NoGameObjectView view = new NoGameObjectView(e.getHolder());
    recipient.sendMessage(view.build(player));
  }

  @Override
  public Set<Class<? extends CommandBase>> getCommandClasses() {
    return ImmutableSet.of(
        AttackShowCommand.class,
        ConfigureActorCommand.class,
        ConfigureTargetCommand.class
    );
  }
}
