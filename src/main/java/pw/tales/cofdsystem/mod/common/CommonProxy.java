package pw.tales.cofdsystem.mod.common;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public abstract class CommonProxy {

  protected final CommonGuiceModule guiceModule;
  protected final Injector injector;

  protected CommonProxy() {
    this.guiceModule = this.createGuiceModule();
    this.injector = Guice.createInjector(this.guiceModule);
  }

  protected abstract CommonGuiceModule createGuiceModule();

  public void setUp(FMLPreInitializationEvent event) {
    NetworkHelper.getInstance().initMessageHandlers();
  }

  public void onServerStarted(FMLServerStartingEvent event) {
  }
}