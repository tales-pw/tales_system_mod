package pw.tales.cofdsystem.mod.common.modules.equipment.network.messages;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TooltipResponseMessage implements IMessage {

  private UUID requestID;
  private ITextComponent tooltip;

  public TooltipResponseMessage() {
  }

  public TooltipResponseMessage(UUID requestID, ITextComponent tooltip) {
    this.requestID = requestID;
    this.tooltip = tooltip;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    PacketBuffer packetBuffer = new PacketBuffer(buf);
    try {
      this.requestID = packetBuffer.readUniqueId();
      this.tooltip = packetBuffer.readTextComponent();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    PacketBuffer packetBuffer = new PacketBuffer(buf);
    packetBuffer.writeUniqueId(this.requestID);
    packetBuffer.writeTextComponent(this.tooltip);
  }

  public ITextComponent getTooltip() {
    return this.tooltip;
  }

  public UUID getRequestID() {
    return this.requestID;
  }
}
