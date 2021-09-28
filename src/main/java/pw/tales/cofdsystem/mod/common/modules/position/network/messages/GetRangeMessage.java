package pw.tales.cofdsystem.mod.common.modules.position.network.messages;

import net.minecraft.entity.Entity;
import pw.tales.cofdsystem.mod.common.network.messages.TargetsMessage;

public class GetRangeMessage extends TargetsMessage {

  public GetRangeMessage() {
  }

  public GetRangeMessage(Entity[] entities) {
    super(entities);
  }
}
