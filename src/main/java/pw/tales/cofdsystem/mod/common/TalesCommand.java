package pw.tales.cofdsystem.mod.common;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import pw.tales.cofdsystem.exceptions.CofDSystemException;


public abstract class TalesCommand extends CommandBase {

  private final String name;

  protected TalesCommand(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return this.name;
  }

  protected void sendError(ICommandSender sender, CommandException commandexception) {
    ITextComponent component = new TextComponentTranslation(
        commandexception.getMessage(), commandexception.getErrorObjects()
    );
    this.applyErrorStyle(component);
    sender.sendMessage(component);
  }

  protected void applyErrorStyle(ITextComponent component) {
    component.getStyle().setColor(TextFormatting.RED);
  }

  @Override
  public void execute(
      MinecraftServer server,
      ICommandSender sender,
      String[] args
  ) throws CommandException {
    try {
      this.wrappedExecute(server, sender, args);
    } catch (CofDSystemException e) {
      this.handleSystemException(server, sender, e);
    }
  }

  protected void handleSystemException(
      MinecraftServer server,
      ICommandSender sender,
      CofDSystemException systemException
  ) {
    try {
      throw systemException;
    } catch (CofDSystemException e) {
      ITextComponent component = new TextComponentTranslation(
          "command.system.error.unknown_system_error",
          e.getMessage()
      );
      this.applyErrorStyle(component);
      sender.sendMessage(component);
      throw e;
    }
  }

  public abstract void wrappedExecute(
      MinecraftServer server,
      ICommandSender sender,
      String[] args
  ) throws CommandException, CofDSystemException;

  @Override
  public String getUsage(ICommandSender sender) {
    return this.name + ".usage";
  }
}
