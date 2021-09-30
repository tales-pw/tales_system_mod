package pw.tales.cofdsystem.mod.server.modules.attack.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.UUID;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import pw.tales.cofdsystem.common.EnumSide;
import pw.tales.cofdsystem.mod.server.modules.attack.Attack;
import pw.tales.cofdsystem.mod.server.modules.attack.AttackManager;
import pw.tales.cofdsystem.mod.server.modules.attack.views.TargetMenuView;
import pw.tales.cofdsystem.mod.server.modules.gui_windows.WindowsModule;
import pw.tales.cofdsystem.mod.server.views.View;


@Singleton
public class ConfigureTargetCommand extends ConfigureCommand {

  public static final String NAME = "_s.attack.configure.resist";

  @Inject
  public ConfigureTargetCommand(WindowsModule windowsModule, AttackManager attackManager) {
    super(NAME, EnumSide.TARGET, windowsModule, attackManager);
  }

  public static String generate(UUID uuid, ConfigureAction action, Object arg) {
    return String.format(
        "/%s %s %s %s",
        ConfigureTargetCommand.NAME,
        uuid,
        action,
        arg
    );
  }

  @Override
  public String getName() {
    return ConfigureTargetCommand.NAME;
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "command.resist.usage";
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return true;
  }

  @Override
  protected View createWindowView(
      EntityPlayerMP player,
      Attack attack
  ) {
    return new TargetMenuView(attack);
  }
}
