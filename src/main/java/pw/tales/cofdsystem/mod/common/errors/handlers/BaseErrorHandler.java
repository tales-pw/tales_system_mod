package pw.tales.cofdsystem.mod.common.errors.handlers;

import net.minecraft.command.ICommandSender;
import pw.tales.cofdsystem.mod.common.errors.IErrorHandler;

public abstract class BaseErrorHandler<T extends Throwable> implements IErrorHandler {

  private final Class<T> errorClazz;

  protected BaseErrorHandler(Class<T> errorClazz) {
    this.errorClazz = errorClazz;
  }

  @Override
  public final boolean handle(ICommandSender recipient, Throwable e) {
    if (!this.shouldHandle(e)) {
      return false;
    }

    this.handleError(recipient, this.getErrorClass().cast(e));

    return true;
  }

  protected boolean shouldHandle(Throwable e) {
    return this.getErrorClass().isAssignableFrom(e.getClass());
  }

  public Class<T> getErrorClass() {
    return this.errorClazz;
  }

  public abstract void handleError(ICommandSender recipient, T e);
}
