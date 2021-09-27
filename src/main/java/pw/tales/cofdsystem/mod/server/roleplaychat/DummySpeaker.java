package pw.tales.cofdsystem.mod.server.roleplaychat;

import java.util.UUID;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import ru.xunto.roleplaychat.api.IPermission;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.api.IWorld;
import ru.xunto.roleplaychat.api.Position;
import ru.xunto.roleplaychat.forge_1_12_2.ForgeWorld;
import ru.xunto.roleplaychat.framework.renderer.text.Text;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;

public class DummySpeaker implements ISpeaker {

  private final UUID uuid = UUID.randomUUID();
  private final WorldServer server;

  public DummySpeaker() {
    this.server = (WorldServer) FMLCommonHandler.instance().getMinecraftServerInstance()
        .getEntityWorld();
  }

  @Override
  public void sendMessage(String s, TextColor textColor) {
    // DummySpeaker don't need to receive messages.
  }

  @Override
  public void sendMessage(Text text) {
    // DummySpeaker don't need to receive messages.
  }

  @Override
  public String getName() {
    return "dummy";
  }

  @Override
  public String getRealName() {
    return "dummy";
  }

  @Override
  public Position getPosition() {
    return new Position(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
  }

  @Override
  public IWorld getWorld() {
    return new ForgeWorld(server);
  }

  @Override
  public UUID getUniqueID() {
    return this.uuid;
  }

  @Override
  public boolean hasPermission(IPermission permission) {
    return false;
  }

  @Override
  public boolean hasPermission(String s) {
    return false;
  }
}
