package pw.tales.cofdsystem.mod.common;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public interface IModule {

  default void setUp() {

  }

  default void onServerStart(FMLServerStartingEvent event) {

  }
}
