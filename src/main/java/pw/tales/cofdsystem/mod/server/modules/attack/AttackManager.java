package pw.tales.cofdsystem.mod.server.modules.attack;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.action_attack.builder.AttackBuilder;
import pw.tales.cofdsystem.common.EnumSide;
import pw.tales.cofdsystem.game_object.GameObject;

@Singleton
public class AttackManager {

  private final HashMap<UUID, AttackBuilder> builders = new HashMap<>();

  private final Set<UUID> attackerConfirmation = new HashSet<>();
  private final Set<UUID> targetConfirmation = new HashSet<>();

  private final CofDSystem system;

  @Inject
  public AttackManager(CofDSystem system) {
    this.system = system;
  }

  public UUID save(AttackBuilder builder) {
    UUID uuid = UUID.randomUUID();
    builders.put(uuid, builder);
    return uuid;
  }

  public void finish(UUID uuid) {
    AttackBuilder builder = this.fetch(uuid);
    Objects.requireNonNull(builder);

    system.act(builder.build());

    builders.remove(uuid);
    attackerConfirmation.remove(uuid);
    targetConfirmation.remove(uuid);
  }

  @Nullable
  public AttackBuilder fetch(UUID uuid) {
    return builders.get(uuid);
  }

  public boolean isConfirmed(UUID uuid) {
    return isConfirmed(uuid, EnumSide.ACTOR) && isConfirmed(uuid, EnumSide.TARGET);
  }

  public boolean isConfirmed(UUID uuid, EnumSide side) {
    Set<UUID> confirmation = this.getSideConfirmation(side);
    return confirmation.contains(uuid);
  }

  private Set<UUID> getSideConfirmation(EnumSide side) {
    if (side == EnumSide.ACTOR) {
      return attackerConfirmation;
    }
    return targetConfirmation;
  }

  public void confirm(UUID uuid, EnumSide side) {
    Set<UUID> confirmation = this.getSideConfirmation(side);
    confirmation.add(uuid);
  }

  public void clearGOAttacks(GameObject gameObject) {
    for (Entry<UUID, AttackBuilder> entry : this.builders.entrySet()) {
      UUID uuid = entry.getKey();
      AttackBuilder builder = entry.getValue();

      if (builder.isRelated(gameObject)) {
        this.builders.remove(uuid);
      }

    }
  }
}
