package pw.tales.cofdsystem.mod.common.network.messages;

import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import pw.tales.cofdsystem.mod.common.network.MessageUtils;

public class TargetsMessage implements IMessage {

  private Entity[] entities;

  public TargetsMessage() {
  }

  public TargetsMessage(Entity[] entities) {
    this.entities = entities;
  }

  public Entity[] getEntities() {
    return this.entities;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    int amount = buf.readInt();

    this.entities = new Entity[amount];
    for (int i1 = 0; i1 < amount; i1++) {
      UUID uuid = UUID.fromString(MessageUtils.readString(buf));
      this.entities[i1] = server.getEntityFromUuid(uuid);
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(entities.length);
    for (Entity entity : entities) {
      MessageUtils.writeString(buf, entity.getPersistentID().toString());
    }
  }
}
