package pw.tales.cofdsystem.mod.common.modules.gui_windows.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SystemWindowMessage implements IMessage {
  public static final int MAX_IDENTIFIER_LEN = 50;

  private String id = null;

  public SystemWindowMessage() {
  }

  public SystemWindowMessage(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    PacketBuffer packetBuffer = new PacketBuffer(buf);
    this.id = packetBuffer.readString(MAX_IDENTIFIER_LEN);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    PacketBuffer packetBuffer = new PacketBuffer(buf);
    packetBuffer.writeString(this.id);
  }

  public boolean isValid() {
    return this.id != null;
  }
}
