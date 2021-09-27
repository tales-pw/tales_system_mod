package pw.tales.cofdsystem.mod.server.modules.attack.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.UUID;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import pw.tales.cofdsystem.action_attack.builder.AttackBuilder;
import pw.tales.cofdsystem.common.EnumSide;
import pw.tales.cofdsystem.mod.common.TalesCommand;
import pw.tales.cofdsystem.mod.server.modules.attack.AttackManager;
import pw.tales.cofdsystem.mod.server.modules.attack.views.ActorMenuView;
import pw.tales.cofdsystem.mod.server.modules.attack.views.TargetMenuView;
import pw.tales.cofdsystem.mod.server.modules.gui_windows.WindowsModule;
import pw.tales.cofdsystem.mod.server.views.View;


@Singleton
public class AttackShowCommand extends TalesCommand {

  private static final String NAME = "_s.attack.show";

  private final AttackManager attackManager;
  private final WindowsModule windowsModule;

  @Inject
  public AttackShowCommand(
      AttackManager attackManager,
      WindowsModule windowsModule
  ) {
    super(NAME);
    this.attackManager = attackManager;
    this.windowsModule = windowsModule;
  }

  public static String generate(UUID uuid, EnumSide side) {
    return String.format("/%s %s %s", NAME, uuid, side.getName());
  }

  @Override
  public void wrappedExecute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    EntityPlayerMP entity = getCommandSenderAsPlayer(sender);

    if (args.length < 2) {
      throw new CommandException("command.attack.show.bind.not_enough_arguments");
    }

    UUID uuid = UUID.fromString(args[0]);
    AttackBuilder attack = attackManager.fetch(uuid);

    if (attack == null) {
      throw new CommandException("command.configure.builder_not_found");
    }

    EnumSide side = EnumSide.byName(args[1]);

    View view;
    if (side == EnumSide.ACTOR) {
      view = new ActorMenuView(uuid, this.attackManager, attack);
    } else if (side == EnumSide.TARGET) {
      view = new TargetMenuView(uuid, this.attackManager, attack);
    } else {
      return;
    }

    this.windowsModule.updateWindow(
        entity,
        view,
        uuid.toString(),
        false
    );
  }
}
