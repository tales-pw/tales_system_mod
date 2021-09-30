package pw.tales.cofdsystem.mod.common.network.messages;

import io.netty.buffer.ByteBuf;
import java.util.Arrays;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TargetsMessage implements IMessage {

  private UUID[] uuids;

  public TargetsMessage() {
  }

  public TargetsMessage(Entity[] entities) {
    this(
        Arrays.stream(entities)
            .map(Entity::getPersistentID)
            .toArray(UUID[]::new)
    );
  }

  public TargetsMessage(UUID[] uuids) {
    this.uuids = uuids;
  }

  public Entity[] getEntities() {
    // TODO: Probably should be moved to handler
    MinecraftServer server = FMLCommonHandler.instance()
        .getMinecraftServerInstance();

    return Arrays.stream(this.uuids)
        .map(server::getEntityFromUuid)
        .toArray(Entity[]::new);
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    PacketBuffer packetBuffer = new PacketBuffer(buf);

    int amount = packetBuffer.readInt();
    this.uuids = new UUID[amount];
    for (int i1 = 0; i1 < amount; i1++) {
      this.uuids[i1] = packetBuffer.readUniqueId();
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    PacketBuffer packetBuffer = new PacketBuffer(buf);

    packetBuffer.writeInt(uuids.length);
    for (UUID uuid : uuids) {
      packetBuffer.writeUniqueId(uuid);
    }
  }
}
