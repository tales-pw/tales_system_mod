package pw.tales.cofdsystem.mod.common;

import com.google.inject.AbstractModule;
import net.minecraftforge.fml.common.FMLCommonHandler;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.mod.common.errors.IErrorHandler;
import pw.tales.cofdsystem.mod.common.errors.handlers.DefaultErrorHandler;
import pw.tales.cofdsystem.mod.common.network.TalesMessageHandler;

public class CommonGuiceModule extends AbstractModule {

  private final CofDSystem cofdSystem;

  public CommonGuiceModule(CofDSystem cofdSystem) {
    this.cofdSystem = cofdSystem;
  }

  @Override
  protected void configure() {
    bind(FMLCommonHandler.class).toInstance(FMLCommonHandler.instance());
    bind(CofDSystem.class).toInstance(this.cofdSystem);

    this.bindErrorHandler();

    requestStaticInjection(TalesMessageHandler.class);
    requestStaticInjection(TalesCommand.class);
  }

  protected void bindErrorHandler() {
    bind(IErrorHandler.class).to(DefaultErrorHandler.class);
  }
}
