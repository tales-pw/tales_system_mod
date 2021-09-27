package pw.tales.cofdsystem.mod.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import pw.tales.cofdsystem.dices.EnumExplode;
import pw.tales.cofdsystem.dices.IRollRequest;
import pw.tales.cofdsystem.dices.requests.RollRequestPool;
import pw.tales.cofdsystem.mod.common.network.MessageUtils;

public class SimpleRollMessage implements IMessage {

  @SuppressWarnings("java:S1104")
  public IRollRequest request;

  public SimpleRollMessage() {
  }

  public SimpleRollMessage(IRollRequest request) {
    this.request = request;
  }

  @Override
  public void fromBytes(ByteBuf byteBuf) {
    int pool = byteBuf.readInt();
    String explodeName = MessageUtils.readString(byteBuf);
    EnumExplode explode = EnumExplode.findByName(explodeName);

    RollRequestPool poolRequest = new RollRequestPool(pool);
    poolRequest.setExplode(explode);

    this.request = poolRequest;
  }

  @Override
  public void toBytes(ByteBuf byteBuf) {
    byteBuf.writeInt(request.getPoolSize());
    MessageUtils.writeString(byteBuf, request.getExplode().getName());
  }
}
