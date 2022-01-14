package pw.tales.cofdsystem.mod.server.modules.remote_binding.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import pw.tales.cofdsystem.exceptions.CofDSystemException;
import pw.tales.cofdsystem.mod.common.TalesCommand;
import pw.tales.cofdsystem.mod.server.modules.remote_binding.RemoteBindingModule;

@Singleton
public class ForceRemoteBindingCommand extends TalesCommand {

  private static final String NAME = "s.go.remote_binding.force";

  private final RemoteBindingModule remoteBindingModule;

  @Inject
  protected ForceRemoteBindingCommand(RemoteBindingModule remoteBindingModule) {
    super(NAME);
    this.remoteBindingModule = remoteBindingModule;
  }

  @Override
  public void wrappedExecute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException, CofDSystemException {
    EntityPlayer target;
    if (args.length == 0) {
      target = CommandBase.getCommandSenderAsPlayer(sender);
    } else if (args.length == 1) {
      target = CommandBase.getPlayer(server, sender, args[0]);
    } else {
      throw new CommandException("command.force_remote_binding.wrong_usage");
    }

    ITextComponent startMsg = new TextComponentTranslation(
        "command.force_remote_binding.start"
    );
    startMsg.getStyle().setColor(TextFormatting.GREEN);
    sender.sendMessage(startMsg);

    this.remoteBindingModule.applyRemoteBinding(target).thenApply(
        gameObject -> {
          ITextComponent msg = new TextComponentTranslation(
              "command.force_remote_binding.success",
              gameObject.getDN()
          );
          msg.getStyle().setColor(TextFormatting.GREEN);
          sender.sendMessage(msg);
          return gameObject;
        }
    ).exceptionally(e -> {
      TalesCommand.errors.handle(sender, e);
      return null;
    });
  }
}
