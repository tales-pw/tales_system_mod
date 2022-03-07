package pw.tales.cofdsystem.mod.server.modules.go_relation_entity.exceptions;

import net.minecraft.entity.Entity;
import pw.tales.cofdsystem.game_object.GameObject;

public class AlreadyAttachedException extends RuntimeException {
  private final Entity entity;
  private final GameObject gameObject;
  private final Entity attachedEntity;

  public AlreadyAttachedException(Entity entity, GameObject gameObject, Entity attachedEntity) {
    this.entity = entity;
    this.gameObject = gameObject;
    this.attachedEntity = attachedEntity;
  }

  public Entity getTargetEntity() {
    return this.entity;
  }

  public Entity getAttachedEntity() {
    return this.attachedEntity;
  }

  public GameObject getGameObject() {
    return this.gameObject;
  }
}
