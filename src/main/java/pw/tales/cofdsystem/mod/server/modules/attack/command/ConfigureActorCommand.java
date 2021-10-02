package pw.tales.cofdsystem.mod.server.modules.attack.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.UUID;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import pw.tales.cofdsystem.common.EnumSide;
import pw.tales.cofdsystem.mod.server.modules.attack.AttackManager;
import pw.tales.cofdsystem.mod.server.modules.attack.AttackNotifications;


@Singleton
public class ConfigureActorCommand extends ConfigureCommand {

  public static final String NAME = "_s.attack.configure";

  @Inject
  public ConfigureActorCommand(
      AttackNotifications attackNotifications,
      AttackManager attackManager
  ) {
    super(NAME, EnumSide.ACTOR, attackNotifications, attackManager);
  }

  public static String generate(UUID uuid, ConfigureAction action, Object arg) {
    return String.format(
        "/%s %s %s %s",
        ConfigureActorCommand.NAME,
        uuid,
        action,
        arg
    );
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "command.attack.usage";
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return true;
  }
}
