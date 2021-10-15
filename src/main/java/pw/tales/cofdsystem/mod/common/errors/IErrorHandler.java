package pw.tales.cofdsystem.mod.common.errors;

import net.minecraft.command.ICommandSender;

public interface IErrorHandler {

  boolean handle(ICommandSender recipient, Throwable e);
}
