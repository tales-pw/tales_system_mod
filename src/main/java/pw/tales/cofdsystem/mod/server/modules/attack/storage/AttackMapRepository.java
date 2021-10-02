package pw.tales.cofdsystem.mod.server.modules.attack.storage;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import pw.tales.cofdsystem.action_attack.builder.AttackBuilder;
import pw.tales.cofdsystem.mod.server.modules.attack.Attack;

/**
 * Attack repository that stores attacks in simple hashmap.
 */
@Singleton
public class AttackMapRepository implements IAttackRepository {

  private final Map<UUID, Attack> attacks = new HashMap<>();

  @Inject
  public AttackMapRepository() {
    // Nothing to set in constructor.
  }

  /**
   * Save AttackBuilder as new attack.
   *
   * @param builder AttackBuilder.
   * @return New Attack instance.
   */
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

  /**
   * Fetch Attack with specified UUID.
   *
   * @param uuid UUID of attack.
   * @return Attack.
   */
  @Nullable
  public Attack fetch(UUID uuid) {
    return this.attacks.get(uuid);
  }

  /**
   * Get all stored Attacks.
   */
  public Set<Attack> getAll() {
    return new HashSet<>(this.attacks.values());
  }

  /**
   * Remove Attack with specified UUID.
   *
   * @param uuid UUID of attack.
   */
  public void remove(UUID uuid) {
    this.attacks.remove(uuid);
  }
}
