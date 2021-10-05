package pw.tales.cofdsystem.mod.server.modules.attack;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.entity.player.EntityPlayer;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.action.competition.Competition;
import pw.tales.cofdsystem.action_attack.AttackAction;
import pw.tales.cofdsystem.action_attack.events.AttackInitiatedEvent;
import pw.tales.cofdsystem.action_attack.events.AttackMissEvent;
import pw.tales.cofdsystem.common.EnumSide;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.common.IModule;
import pw.tales.cofdsystem.mod.common.haxe_adapters.HaxeFn;
import pw.tales.cofdsystem.mod.server.modules.attack.rpchat_messages.AttackInitiatedMessage;
import pw.tales.cofdsystem.mod.server.modules.attack.rpchat_messages.AttackMissMessage;
import pw.tales.cofdsystem.mod.server.modules.attack.views.ActorMenuView;
import pw.tales.cofdsystem.mod.server.modules.attack.views.MenuView;
import pw.tales.cofdsystem.mod.server.modules.attack.views.TargetMenuView;
import pw.tales.cofdsystem.mod.server.modules.gui_windows.WindowsModule;
import pw.tales.cofdsystem.mod.server.modules.notification.NotificationModule;
import pw.tales.cofdsystem.mod.server.modules.operators.OperatorsModule;
import pw.tales.cofdsystem.utils.events.HandlerPriority;

@Singleton
public class AttackNotifications implements IModule {

  private final CofDSystem system;
  private final OperatorsModule operatorsModule;
  private final WindowsModule windowsModule;
  private final NotificationModule notificationModule;

  @Inject
  public AttackNotifications(
      CofDSystem system,
      OperatorsModule operatorsModule,
      WindowsModule windowsModule,
      NotificationModule notificationModule
  ) {
    this.system = system;
    this.operatorsModule = operatorsModule;
    this.windowsModule = windowsModule;
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

    Competition competition = attackAction.getCompetition();
    GameObject actor = competition.getActor();
    GameObject target = competition.getTarget();

    this.notificationModule.sendGOAsOrigin(
        new AttackMissMessage(actor),
        actor, target
    );
  }

  public void onAttackInitiatedEvent(AttackInitiatedEvent event) {
    AttackAction attackAction = event.getAttackAction();

    Competition competition = attackAction.getCompetition();
    GameObject actor = competition.getActor();
    GameObject target = competition.getTarget();

    this.notificationModule.sendGOAsOrigin(
        new AttackInitiatedMessage(actor, target),
        actor, target
    );
  }

  /**
   * Update attack windows for everybody.
   *
   * @param attack    Attack object.
   * @param forceOpen Should window be forcefully opened for attacker and target (but not
   *                  operator).
   */
  public void updateWindows(Attack attack, boolean forceOpen) {
    this.updateSideWindows(attack, EnumSide.ACTOR, forceOpen);
    this.updateSideWindows(attack, EnumSide.TARGET, forceOpen);
  }

  /**
   * Update attack window for side.
   *
   * @param attack    Attack object.
   * @param side      Attack side (attacker of target).
   * @param forceOpen Should window be forcefully opened for side (but not operator).
   */
  public void updateSideWindows(Attack attack, EnumSide side, boolean forceOpen) {
    GameObject gameObject = attack.getSideGO(side);
    String windowId = attack.getWindowDN(side);
    MenuView menuView = this.getSideView(attack, side);

    this.notificationModule.updateGoWindow(
        menuView,
        windowId,
        gameObject,
        forceOpen
    );

    this.operatorsModule.updateWindow(
        menuView,
        windowId
    );
  }

  /**
   * Get view for specific side.
   *
   * @param attack Attack object.
   * @param side   Attack side.
   * @return View.
   */
  private MenuView getSideView(Attack attack, EnumSide side) {
    if (side == EnumSide.ACTOR) {
      return new ActorMenuView(attack);
    } else {
      return new TargetMenuView(attack);
    }
  }

  /**
   * Remove window for everybody.
   *
   * @param attack Attack object.
   */
  public void removeWindowsForAll(Attack attack) {
    this.windowsModule.removeWindowForAll(attack.getWindowDN(EnumSide.ACTOR));
    this.windowsModule.removeWindowForAll(attack.getWindowDN(EnumSide.TARGET));
  }

  /**
   * Forcefully open window for entity (used to switch to specific side window).
   *
   * @param attack Attack object.
   * @param player Entity.
   * @param side   Attack side.
   */
  public void forceOpenWindow(Attack attack, EntityPlayer player, EnumSide side) {
    String windowId = attack.getWindowDN(side);
    MenuView menuView = this.getSideView(attack, side);

    this.windowsModule.updateWindow(
        player,
        menuView,
        windowId,
        true
    );
  }
}
