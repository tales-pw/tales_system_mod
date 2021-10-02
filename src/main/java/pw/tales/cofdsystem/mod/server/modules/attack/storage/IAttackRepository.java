package pw.tales.cofdsystem.mod.server.modules.attack.storage;

import java.util.Collection;
import java.util.UUID;
import javax.annotation.Nullable;
import pw.tales.cofdsystem.action_attack.builder.AttackBuilder;
import pw.tales.cofdsystem.mod.server.modules.attack.Attack;

/**
 * Interface for attack storage.
 */
public interface IAttackRepository {

  /**
   * Save AttackBuilder as new attack.
   *
   * @param builder AttackBuilder.
   * @return New Attack instance.
   */
  Attack save(AttackBuilder builder);

  /**
   * Fetch Attack with specified UUID.
   *
   * @param uuid UUID of attack.
   * @return Attack.
   */
  @Nullable
  Attack fetch(UUID uuid);

  /**
   * Get all stored Attacks.
   */
  Collection<Attack> getAll();

  /**
   * Removed specified Attack.
   *
   * @param attack Attack.
   */
  default void remove(Attack attack) {
    this.remove(attack.getId());
  }

  /**
   * Remove Attack with specified UUID.
   *
   * @param uuid UUID of attack.
   */
  void remove(UUID uuid);
}
