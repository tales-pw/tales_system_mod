package pw.tales.cofdsystem.mod.server.modules.simple_roll.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import pw.tales.cofdsystem.dices.EnumExplode;
import pw.tales.cofdsystem.dices.RollRequest;
import pw.tales.cofdsystem.mod.server.modules.simple_roll.SimpleRollModule;
import pw.tales.cofdsystem.utils.math.MathValue;


@Singleton
public class SimpleRollCommand extends CommandBase {

  private final SimpleRollModule simpleRollModule;

  @Inject
  public SimpleRollCommand(SimpleRollModule simpleRollModule) {
    this.simpleRollModule = simpleRollModule;
  }

  @Override
  public String getName() {
    return "roll";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "command.roll.usage";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {

    if (args.length < 1) {
      throw new CommandException("command.error.no_arguments");
    }

    List<String> argsList = new ArrayList<>(Arrays.asList(args));

    String flag = "";
    if (argsList.get(0).startsWith("-")) {
      flag = argsList.remove(0);
    }

    int poolSize;
    try {
      poolSize = Integer.parseInt(argsList.get(0));
    } catch (NumberFormatException e) {
      throw new CommandException("command.error.pool_size_integer");
    }

    if (poolSize < 0 || poolSize > 50) {
      throw new CommandException("command.error.pool_size_border");
    }

    EnumExplode explode = EnumExplode.DEFAULT;
    if (flag.equals("-")) {
      explode = EnumExplode.NONE;
    }
    if (flag.equals("-9")) {
      explode = EnumExplode.NINE_AGAIN;
    }
    if (flag.equals("-8")) {
      explode = EnumExplode.EIGHT_AGAIN;
    }
    if (flag.equals("-r")) {
      explode = EnumExplode.ROTE_ACTION;
    }

    RollRequest request = new RollRequest(new MathValue<>(poolSize));
    request.setExplode(explode);

    this.simpleRollModule.roll((ServerPlayerEntity) sender, request);
  }

  @Override
  public int getRequiredPermissionLevel() {
    return 0;
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return true;
  }
}
