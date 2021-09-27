package pw.tales.cofdsystem.mod.common.modules.scene.network.mesages;

import net.minecraft.entity.Entity;
import pw.tales.cofdsystem.mod.common.network.messages.TargetsMessage;

public class SceneAddMessage extends TargetsMessage {

  public SceneAddMessage() {
    super();
  }

  public SceneAddMessage(Entity[] entities) {
    super(entities);
  }
}
