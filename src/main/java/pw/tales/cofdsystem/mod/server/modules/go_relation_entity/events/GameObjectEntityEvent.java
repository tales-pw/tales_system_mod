package pw.tales.cofdsystem.mod.server.modules.go_relation_entity.events;

import net.minecraft.entity.Entity;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.server.modules.go_source.events.GameObjectEvent;

public abstract class GameObjectEntityEvent extends GameObjectEvent {

  private final Entity entity;

  GameObjectEntityEvent(Entity entity, GameObject gameObject) {
    super(gameObject);
    this.entity = entity;
  }

  public Entity getEntity() {
    return entity;
  }
}
