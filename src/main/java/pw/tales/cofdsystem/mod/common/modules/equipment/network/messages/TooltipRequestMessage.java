package pw.tales.cofdsystem.mod.common.modules.equipment.network.messages;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TooltipRequestMessage implements IMessage {

  private UUID requestID;
  private ItemStack itemStack;

  public TooltipRequestMessage() {
  }

  public TooltipRequestMessage(
      UUID requestID,
      ItemStack itemStack
  ) {
    this.requestID = requestID;
    this.itemStack = itemStack;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    PacketBuffer packetBuffer = new PacketBuffer(buf);
    try {
      this.requestID = packetBuffer.readUniqueId();
      this.itemStack = packetBuffer.readItemStack();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    PacketBuffer packetBuffer = new PacketBuffer(buf);
    packetBuffer.writeUniqueId(this.requestID);
    packetBuffer.writeItemStack(itemStack);
  }

  public ItemStack getItemStack() {
    return itemStack;
  }

  public UUID getRequestID() {
    return requestID;
  }
}
