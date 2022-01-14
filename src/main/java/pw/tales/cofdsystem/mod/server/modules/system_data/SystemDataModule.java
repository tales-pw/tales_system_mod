package pw.tales.cofdsystem.mod.server.modules.system_data;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import haxe.lang.Function;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.mod.ModConfig;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.common.IModule;
import pw.tales.cofdsystem.synchronization.api.SystemStorage;

@Singleton
public class SystemDataModule implements IModule {

  private final CofDSystem cofdSystem;
  private boolean loaded = false;

  @Inject
  public SystemDataModule(CofDSystem cofdSystem) {
    this.cofdSystem = cofdSystem;
  }

  @Override
  public void setUp() {
    EVENT_BUS.register(this);
  }

  @Override
  public void onServerStart(FMLServerStartingEvent event) {
    if (!loaded) {
      this.loadRemoteData();
      this.loaded = true;
    }
  }

  private void loadRemoteData() {
    SystemStorage systemStorage = new SystemStorage(ModConfig.systemApiUrl);
    systemStorage.onSuccess = new Function(0, 0) {
      @Override
      public Object __hx_invoke0_o() {
        TalesSystem.logger.info("System loaded: {}", cofdSystem.traits.items().toString());
        return null;
      }
    };

    systemStorage.update(cofdSystem);
  }
}
