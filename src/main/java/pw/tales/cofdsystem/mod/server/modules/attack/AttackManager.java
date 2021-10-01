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

@Singleton
public class AttackManager {
  private final CofDSystem system;
  private final IAttackRepository repository;
  private final AttackNotifications notifications;

  @Inject
  public AttackManager(
      CofDSystem system,
      IAttackRepository repository,
      AttackNotifications notifications
  ) {
    this.repository = repository;
    this.notifications = notifications;
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

    this.notifications.updateWindows(attack);

    return attack;
  }

  public void finish(UUID uuid) {
    Attack attack = this.repository.fetch(uuid);
    Objects.requireNonNull(attack);

    attack.execute(this.system);

    this.repository.remove(uuid);
    this.notifications.closeWindows(attack);
  }

  @Nullable
  public Attack fetch(UUID uuid) {
    return this.repository.fetch(uuid);
  }

  public void clearGOAttacks(GameObject gameObject) {
    for (Attack attack : this.repository.getAll()) {
      if (attack.isRelated(gameObject)) {
        this.repository.remove(attack);
        this.notifications.closeWindows(attack);
      }
    }
  }
}
