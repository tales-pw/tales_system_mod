package pw.tales.cofdsystem.mod.common.modules.attack.network.mesages;

import net.minecraft.entity.Entity;
import pw.tales.cofdsystem.mod.common.network.messages.TargetsMessage;

public class AttackMessage extends TargetsMessage {

  public AttackMessage() {
    super();
  }

  public AttackMessage(Entity[] entities) {
    super(entities);
  }
}
