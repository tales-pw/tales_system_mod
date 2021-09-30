package pw.tales.cofdsystem.mod.server.modules.attack.storage;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import pw.tales.cofdsystem.action_attack.builder.AttackBuilder;
import pw.tales.cofdsystem.mod.server.modules.attack.Attack;

@Singleton
public class AttackMapRepository implements IAttackRepository {

  private final Map<UUID, Attack> attacks = new HashMap<>();

  @Inject
  public AttackMapRepository() {
  }

  public Attack save(AttackBuilder builder) {
    Attack attack = new Attack(
        UUID.randomUUID(),
        builder,
        LocalDateTime.now()
    );

    this.attacks.put(
        attack.getId(),
        attack
    );

    return attack;
  }

  public void remove(UUID uuid) {
    this.attacks.remove(uuid);
  }

  @Nullable
  public Attack fetch(UUID uuid) {
    return attacks.get(uuid);
  }

  public Collection<Attack> getAll() {
    return this.attacks.values();
  }

  public void remove(Attack attack) {
    this.attacks.remove(attack.getId());
  }
}
