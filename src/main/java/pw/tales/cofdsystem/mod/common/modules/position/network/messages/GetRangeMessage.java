package pw.tales.cofdsystem.mod.common.modules.position.network.messages;

import java.util.UUID;
import pw.tales.cofdsystem.mod.common.network.messages.TargetsMessage;

public class GetRangeMessage extends TargetsMessage {

  public GetRangeMessage() {
  }

  public GetRangeMessage(UUID[] uuids) {
    super(uuids);
  }
}
