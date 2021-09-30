package pw.tales.cofdsystem.mod.server.modules.attack.storage;

import java.util.Collection;
import java.util.UUID;
import javax.annotation.Nullable;
import pw.tales.cofdsystem.action_attack.builder.AttackBuilder;
import pw.tales.cofdsystem.mod.server.modules.attack.Attack;

public interface IAttackRepository {

  Attack save(AttackBuilder builder);

  void remove(UUID uuid);

  @Nullable
  Attack fetch(UUID uuid);

  Collection<Attack> getAll();

  void remove(Attack attack);
}
