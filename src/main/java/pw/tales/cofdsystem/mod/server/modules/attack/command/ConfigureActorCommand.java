package pw.tales.cofdsystem.mod.server.modules.attack.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.UUID;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import pw.tales.cofdsystem.action_attack.builder.AttackBuilder;
import pw.tales.cofdsystem.common.EnumSide;
import pw.tales.cofdsystem.mod.server.modules.attack.AttackManager;
import pw.tales.cofdsystem.mod.server.modules.attack.views.ActorMenuView;
import pw.tales.cofdsystem.mod.server.modules.gui_windows.WindowsModule;
import pw.tales.cofdsystem.mod.server.views.View;


@Singleton
public class ConfigureActorCommand extends ConfigureCommand {

  public static final String NAME = "_s.attack.configure";

  @Inject
  public ConfigureActorCommand(WindowsModule windowsModule, AttackManager attackManager) {
    super(NAME, EnumSide.ACTOR, windowsModule, attackManager);
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
  public String getName() {
    return ConfigureActorCommand.NAME;
  }

  @Override

  public String getUsage(ICommandSender sender) {
    return "command.attack.usage";
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return true;
  }

  @Override
  protected View createWindowView(
      EntityPlayerMP player,
      UUID uuid,
      AttackBuilder builder
  ) {
    return new ActorMenuView(uuid, this.attackManager, builder);
  }
}
