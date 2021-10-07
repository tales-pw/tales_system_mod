package pw.tales.cofdsystem.mod.common;

import com.google.inject.Inject;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import pw.tales.cofdsystem.exceptions.CofDSystemException;
import pw.tales.cofdsystem.mod.common.errors.IErrorHandler;


public abstract class TalesCommand extends CommandBase {

  @Inject
  public static IErrorHandler errors;

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
    } catch (CommandException exception) {
      throw exception;
    } catch (Exception e) {
      this.handleErrors(sender, e);
    }
  }

  protected void handleErrors(ICommandSender sender, Throwable e) {
    errors.handle(sender, e);
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
