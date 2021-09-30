package pw.tales.cofdsystem.mod.server.modules.attack;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.action_attack.builder.AttackBuilder;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.server.modules.attack.storage.IAttackRepository;
import pw.tales.cofdsystem.mod.server.modules.attack.views.ActorMenuView;
import pw.tales.cofdsystem.mod.server.modules.attack.views.TargetMenuView;
import pw.tales.cofdsystem.mod.server.modules.gui_windows.WindowsModule;
import pw.tales.cofdsystem.mod.server.modules.notification.NotificationModule;
import pw.tales.cofdsystem.mod.server.modules.operators.OperatorsModule;

@Singleton
public class AttackManager {

  private final IAttackRepository repository;
  private final WindowsModule windowsModule;
  private final NotificationModule notificationModule;
  private final OperatorsModule operatorsModule;
  private final CofDSystem system;

  @Inject
  public AttackManager(
      IAttackRepository repository,
      WindowsModule windowsModule,
      NotificationModule notificationModule,
      OperatorsModule operatorsModule,
      CofDSystem system
  ) {
    this.repository = repository;
    this.windowsModule = windowsModule;
    this.notificationModule = notificationModule;
    this.operatorsModule = operatorsModule;
    this.system = system;
  }

  public Attack create(
      GameObject attacker,
      GameObject target
  ) {
    AttackBuilder builder = new AttackBuilder(
        attacker,
        target
    );

    Attack attack = this.repository.save(builder);

    this.notificationModule.updateGoWindow(
        new ActorMenuView(attack),
        attack.getWindowId(),
        attacker,
        true
    );

    this.notificationModule.updateGoWindow(
        new TargetMenuView(attack),
        attack.getWindowId(),
        target,
        true
    );

    this.operatorsModule.showWindow(
        new ActorMenuView(attack),
        attack.getWindowId()
    );

    return attack;
  }

  public void finish(UUID uuid) {
    Attack attack = this.repository.fetch(uuid);
    Objects.requireNonNull(attack);

    attack.execute(this.system);

    this.repository.remove(uuid);

    this.windowsModule.removeWindowForAll(attack.getWindowId());
  }

  @Nullable
  public Attack fetch(UUID uuid) {
    return this.repository.fetch(uuid);
  }

  public void clearGOAttacks(GameObject gameObject) {
    for (Attack attack : this.repository.getAll()) {
      if (attack.isRelated(gameObject)) {
        this.repository.remove(attack);
        this.windowsModule.removeWindowForAll(attack.getWindowId());
      }
    }
  }
}
