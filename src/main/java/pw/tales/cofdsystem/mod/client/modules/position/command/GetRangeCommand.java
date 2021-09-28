package pw.tales.cofdsystem.mod.client.modules.position.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import pw.tales.cofdsystem.exceptions.CofDSystemException;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.client.modules.targets.TargetsList;
import pw.tales.cofdsystem.mod.common.TalesCommand;
import pw.tales.cofdsystem.mod.common.modules.position.network.messages.GetRangeMessage;

@Singleton
public class GetRangeCommand extends TalesCommand {

  private static final String NAME = "s.range";

  private final TargetsList targets;

  @Inject
  protected GetRangeCommand(TargetsList targets) {
    super(NAME);
    this.targets = targets;
  }

  @Override
  public void wrappedExecute(
      MinecraftServer server,
      ICommandSender sender,
      String[] args
  ) throws CommandException, CofDSystemException {
    TalesSystem.network.sendToServer(new GetRangeMessage(this.targets.getEntities()));
  }
}
