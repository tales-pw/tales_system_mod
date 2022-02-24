package pw.tales.cofdsystem.mod.common.modules.go_relation_entity.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import pw.tales.cofdsystem.mod.common.network.MessageUtils;
import pw.tales.cofdsystem.mod.common.network.messages.TargetsMessage;

public class EntityGOBindMessage extends TargetsMessage {

  private String dn;
  private boolean clone;

  public EntityGOBindMessage() {
    super();
  }

  public EntityGOBindMessage(String dn, boolean clone, Entity[] entities) {
    super(entities);
    this.dn = dn;
    this.clone = clone;
  }

  public String getDn() {
    return dn;
  }

  public boolean isClone() {
    return  this.clone;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    super.fromBytes(buf);
    this.dn = MessageUtils.readString(buf);
    this.clone = buf.readBoolean();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    super.toBytes(buf);
    MessageUtils.writeString(buf, this.dn);
    buf.writeBoolean(this.clone);
  }
}
