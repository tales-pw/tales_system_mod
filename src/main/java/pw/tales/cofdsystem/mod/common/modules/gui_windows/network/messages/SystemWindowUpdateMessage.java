package pw.tales.cofdsystem.mod.common.modules.gui_windows.network.messages;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

public class SystemWindowUpdateMessage extends SystemWindowMessage {

  private ITextComponent component = null;
  private boolean forceOpen = false;

  public SystemWindowUpdateMessage() {
  }

  public SystemWindowUpdateMessage(
      String id,
      ITextComponent component,
      boolean forceOpen
  ) {
    super(id);
    this.component = component;
    this.forceOpen = forceOpen;
  }

  public ITextComponent getComponent() {
    return this.component;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    super.fromBytes(buf);

    PacketBuffer packetBuffer = new PacketBuffer(buf);
    try {
      this.component = packetBuffer.readTextComponent();
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.forceOpen = packetBuffer.readBoolean();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    super.toBytes(buf);

    PacketBuffer packetBuffer = new PacketBuffer(buf);
    packetBuffer.writeTextComponent(this.component);
    packetBuffer.writeBoolean(this.forceOpen);
  }

  public boolean isValid() {
    return super.isValid() && this.component != null;
  }

  public boolean getForceOpen() {
    return this.forceOpen;
  }
}
