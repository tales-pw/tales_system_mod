package pw.tales.cofdsystem.mod.server.errors;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.ICommandSender;
import pw.tales.cofdsystem.mod.Utils;
import pw.tales.cofdsystem.mod.common.errors.IErrorHandler;
import pw.tales.cofdsystem.mod.common.errors.handlers.DefaultErrorHandler;
import pw.tales.cofdsystem.mod.server.errors.error_handlers.GOFetchingErrorHandler;
import pw.tales.cofdsystem.mod.server.errors.error_handlers.NotBoundErrorHandler;
import pw.tales.cofdsystem.mod.server.errors.error_handlers.SystemErrorHandler;

@Singleton
public class ServerErrors implements IErrorHandler {

  private static final List<Class<? extends IErrorHandler>> HANDLER_CLASSES = ImmutableList.of(
      NotBoundErrorHandler.class,
      GOFetchingErrorHandler.class,
      SystemErrorHandler.class
  );

  private final IErrorHandler defaultHandler;
  private final List<IErrorHandler> handlers;

  @Inject
  public ServerErrors(Injector injector) {
    this.defaultHandler = injector.getInstance(DefaultErrorHandler.class);

    this.handlers = new ArrayList<>();
    for (Class<? extends IErrorHandler> clazz : HANDLER_CLASSES) {
      IErrorHandler instance = injector.getInstance(clazz);
      this.handlers.add(instance);
    }
  }

  public boolean handle(ICommandSender recipient, Throwable e) {
    Throwable exception = Utils.getFutureException(e);

    for (IErrorHandler handler : this.handlers) {
      boolean isHandled = handler.handle(recipient, exception);

      if (isHandled) {
        return true;
      }
    }

    return this.defaultHandler.handle(recipient, e);
  }
}
