package pw.tales.cofdsystem.mod.common;

import com.google.inject.Guice;
import com.google.inject.Injector;
import java.util.Set;
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

  public void setUpModules(Set<Class<? extends IModule>> modules) {
    for (Class<? extends IModule> module : modules) {
      this.injector.getInstance(module).setUp();
    }
  }

  public void onServerStarted(FMLServerStartingEvent event) {
  }
}