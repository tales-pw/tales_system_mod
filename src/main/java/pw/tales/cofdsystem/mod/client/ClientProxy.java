package pw.tales.cofdsystem.mod.client;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Guice;
import com.google.inject.Injector;
import java.util.Set;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.mod.client.modules.equipment.TooltipClientModule;
import pw.tales.cofdsystem.mod.client.modules.go_relation_entity.ClientGORelationModule;
import pw.tales.cofdsystem.mod.client.modules.gui_system.SystemGuiModule;
import pw.tales.cofdsystem.mod.client.modules.gui_windows.ClientWindowsModule;
import pw.tales.cofdsystem.mod.client.modules.scene.ClientSceneModule;
import pw.tales.cofdsystem.mod.client.modules.targets.TargetsModule;
import pw.tales.cofdsystem.mod.common.IModule;
import pw.tales.cofdsystem.mod.server.ServerProxy;

public class ClientProxy extends ServerProxy {

  private static final Set<Class<? extends IModule>> MODULES = ImmutableSet.of(
      TooltipClientModule.class,
      TargetsModule.class,
      SystemGuiModule.class,
      ClientWindowsModule.class,
      ClientGORelationModule.class,
      ClientSceneModule.class
  );

  private final CofDSystem cofdSystem = new CofDSystem();
  private final ClientGuiceModule guiceModule = new ClientGuiceModule(cofdSystem);
  private final Injector injector = Guice.createInjector(guiceModule);

  @Override
  public void setUp(FMLPreInitializationEvent event) {
    super.setUp(event);

    for (Class<? extends IModule> module : MODULES) {
      injector.getInstance(module).setUp();
    }
  }
}
