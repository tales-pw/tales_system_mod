package pw.tales.cofdsystem.mod.common;

import com.google.inject.AbstractModule;
import net.minecraftforge.fml.common.FMLCommonHandler;
import pw.tales.cofdsystem.CofDSystem;

public class CommonGuiceModule extends AbstractModule {

  private final CofDSystem cofdSystem;

  public CommonGuiceModule(CofDSystem cofdSystem) {
    this.cofdSystem = cofdSystem;
  }

  @Override
  protected void configure() {
    bind(FMLCommonHandler.class).toInstance(FMLCommonHandler.instance());
    bind(CofDSystem.class).toInstance(this.cofdSystem);
  }
}
