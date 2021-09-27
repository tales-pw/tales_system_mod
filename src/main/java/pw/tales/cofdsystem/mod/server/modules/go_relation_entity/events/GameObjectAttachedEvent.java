package pw.tales.cofdsystem.mod.server.modules.go_relation_entity.events;

import net.minecraft.entity.Entity;
import pw.tales.cofdsystem.game_object.GameObject;

public class GameObjectAttachedEvent extends GameObjectEntityEvent {

  public GameObjectAttachedEvent(Entity entity, GameObject gameObject) {
    super(entity, gameObject);
  }
}
