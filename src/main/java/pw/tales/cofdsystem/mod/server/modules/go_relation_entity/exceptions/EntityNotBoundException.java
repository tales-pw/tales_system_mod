package pw.tales.cofdsystem.mod.server.modules.go_relation_entity.exceptions;

import net.minecraft.entity.Entity;
import pw.tales.cofdsystem.mod.server.modules.go_relation.exceptions.NotBoundException;

public class EntityNotBoundException extends NotBoundException {

  private final Entity holder;

  public EntityNotBoundException(Entity holder) {
    super(holder);
    this.holder = holder;
  }

  public Entity getHolder() {
    return holder;
  }
}
