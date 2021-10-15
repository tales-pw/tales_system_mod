package pw.tales.cofdsystem.mod.common;

import com.google.inject.AbstractModule;
import net.minecraftforge.fml.common.FMLCommonHandler;
import pw.tales.cofdsystem.mod.common.network.TalesMessageHandler;

public abstract class CommonGuiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(FMLCommonHandler.class).toInstance(FMLCommonHandler.instance());

    requestStaticInjection(TalesMessageHandler.class);
    requestStaticInjection(TalesCommand.class);
  }
}
