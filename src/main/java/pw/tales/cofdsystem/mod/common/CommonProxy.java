package pw.tales.cofdsystem.mod.common;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import pw.tales.cofdsystem.CofDSystem;

public abstract class CommonProxy {

  public void setUp(FMLPreInitializationEvent event) {
    NetworkHelper.getInstance().initMessageHandlers();
  }

  public void onServerStarted(FMLServerStartingEvent event) {
  }

  public void syncSystem(CofDSystem serializer) {
  }
}