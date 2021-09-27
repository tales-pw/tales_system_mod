package pw.tales.cofdsystem.mod.common.modules.go_relation_entity.network.messages;

import net.minecraft.entity.Entity;
import pw.tales.cofdsystem.mod.common.network.messages.TargetsMessage;

public class EntityGOControlMessage extends TargetsMessage {

  public EntityGOControlMessage() {
    super();
  }

  public EntityGOControlMessage(Entity[] entities) {
    super(entities);
  }
}
