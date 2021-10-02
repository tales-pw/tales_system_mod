package pw.tales.cofdsystem.mod.server.modules.attack;

import java.time.LocalDateTime;
import java.util.UUID;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.action_attack.builder.AttackBuilder;
import pw.tales.cofdsystem.common.EnumSide;
import pw.tales.cofdsystem.game_object.GameObject;

/**
 * Stored Attack representation.
 */
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

  /**
   * Get attack id.
   */
  public UUID getId() {
    return this.id;
  }

  /**
   * Get window DN for attack side.
   *
   * @param side Attack side.
   * @return Window DN.
   */
  public String getWindowDN(EnumSide side) {
    String uuidStr = this.id.toString();
    if (side == EnumSide.ACTOR) {
      return String.format("%s_attacker", uuidStr);
    } else {
      return String.format("%s_target", uuidStr);
    }
  }

  /**
   * Get AttackBuilder.
   */
  public AttackBuilder getBuilder() {
    return builder;
  }

  /**
   * Confirm attack for side.
   *
   * @param side Side.
   */
  public void confirm(EnumSide side) {
    if (side == EnumSide.ACTOR) {
      this.confirmedAttacker = true;
    } else {
      this.confirmedTarget = true;
    }
  }

  /**
   * Get GameObject for side.
   *
   * @param side Side.
   * @return GameObject.
   */
  public GameObject getSideGO(EnumSide side) {
    return this.builder.getGameObject(side);
  }

  /**
   * Check if both participants confirmed attack parameters.
   */
  public boolean isBothConfirmed() {
    return this.confirmedAttacker && this.confirmedTarget;
  }

  /**
   * Check when Attack object was created.
   */
  public LocalDateTime getCreationTime() {
    return creationTime;
  }

  /**
   * Perform attack.
   */
  public void execute(CofDSystem system) {
    system.act(this.builder.build());
  }

  /**
   * Check if GameObject related to this attack.
   */
  public boolean isRelated(GameObject gameObject) {
    return this.builder.isRelated(gameObject);
  }

  /**
   * Check if attack parameters confirmed by side.
   */
  public boolean isConfirmed(EnumSide side) {
    if (side == EnumSide.ACTOR) {
      return this.confirmedAttacker;
    } else {
      return this.confirmedTarget;
    }
  }

  @Override
  public String toString() {
    return String.format("Attack{id=%s}", id);
  }
}
