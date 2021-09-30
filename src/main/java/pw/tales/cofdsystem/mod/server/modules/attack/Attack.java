package pw.tales.cofdsystem.mod.server.modules.attack;

import java.time.LocalDateTime;
import java.util.UUID;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.action_attack.builder.AttackBuilder;
import pw.tales.cofdsystem.common.EnumSide;
import pw.tales.cofdsystem.game_object.GameObject;

public class Attack {

  private final UUID id;
  private final AttackBuilder builder;
  private final LocalDateTime creationTime;

  private boolean confirmedAttacker = false;
  private boolean confirmedTarget = false;

  public Attack(
      UUID id,
      AttackBuilder builder,
      LocalDateTime creationTime
  ) {
    this.id = id;
    this.builder = builder;
    this.creationTime = creationTime;
  }

  public UUID getId() {
    return this.id;
  }

  public String getWindowId() {
    return this.id.toString();
  }

  public AttackBuilder getBuilder() {
    return builder;
  }

  public void confirm(EnumSide side) {
    if (side == EnumSide.ACTOR) {
      this.confirmedAttacker = true;
    } else {
      this.confirmedTarget = true;
    }
  }

  public boolean isBothConfirmed() {
    return this.confirmedAttacker && this.confirmedTarget;
  }

  public LocalDateTime getCreationTime() {
    return creationTime;
  }

  public void execute(CofDSystem system) {
    system.act(this.builder.build());
  }

  public boolean isRelated(GameObject gameObject) {
    return this.builder.isRelated(gameObject);
  }

  public boolean isConfirmed(EnumSide side) {
    if (side == EnumSide.ACTOR) {
      return this.confirmedAttacker;
    } else {
      return this.confirmedTarget;
    }
  }
}
