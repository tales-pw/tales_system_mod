package pw.tales.cofdsystem.mod.server.modules.position;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.google.inject.Singleton;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.math.Vec3d;
import pw.tales.cofdsystem.common.EnumRange;
import pw.tales.cofdsystem.mod.common.IModule;

@Singleton
public class ServerPositionModule implements IModule {

  @Override
  public void setUp() {
    EVENT_BUS.register(this);
  }

  public EnumRange measureDistance(ICommandSender from, ICommandSender to) {
    double distance = this.getDistance(from, to);
    return this.measureDistance(distance);
  }

  public EnumRange measureDistance(double distance) {
    return EnumRange.measure((int) Math.floor(distance));
  }

  public double getDistance(ICommandSender from, ICommandSender to) {
    Vec3d fromPosition = from.getPositionVector();
    Vec3d toPosition = to.getPositionVector();

    return fromPosition.distanceTo(toPosition);
  }
}
