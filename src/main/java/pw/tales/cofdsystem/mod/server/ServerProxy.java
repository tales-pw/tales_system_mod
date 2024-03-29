package pw.tales.cofdsystem.mod.server;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import pw.tales.cofdsystem.mod.common.CommonProxy;
import pw.tales.cofdsystem.mod.common.IModule;
import pw.tales.cofdsystem.mod.server.modules.action_roll.ActionRollModule;
import pw.tales.cofdsystem.mod.server.modules.attack.AttackModule;
import pw.tales.cofdsystem.mod.server.modules.attack.AttackNotifications;
import pw.tales.cofdsystem.mod.server.modules.equipment.EquipmentModule;
import pw.tales.cofdsystem.mod.server.modules.equipment.SystemSlotsModule;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.GOEntityRelation;
import pw.tales.cofdsystem.mod.server.modules.go_relation_item.GOItemRelation;
import pw.tales.cofdsystem.mod.server.modules.go_source_local.LocalGOModule;
import pw.tales.cofdsystem.mod.server.modules.go_source_remote.RemoteGOModule;
import pw.tales.cofdsystem.mod.server.modules.gui_windows.WindowsModule;
import pw.tales.cofdsystem.mod.server.modules.notification.NotificationModule;
import pw.tales.cofdsystem.mod.server.modules.operators.OperatorsModule;
import pw.tales.cofdsystem.mod.server.modules.position.ServerPositionModule;
import pw.tales.cofdsystem.mod.server.modules.remote_binding.RemoteBindingModule;
import pw.tales.cofdsystem.mod.server.modules.scene.SceneModule;
import pw.tales.cofdsystem.mod.server.modules.simple_roll.SimpleRollModule;
import pw.tales.cofdsystem.mod.server.modules.system_data.SystemDataModule;

public class ServerProxy extends CommonProxy {

  private static final Set<Class<? extends IModule>> MODULES = ImmutableSet.of(
      OperatorsModule.class,
      RemoteBindingModule.class,
      SystemDataModule.class,
      GOEntityRelation.class,
      GOItemRelation.class,
      EquipmentModule.class,
      SystemSlotsModule.class,
      ActionRollModule.class,
      SimpleRollModule.class,
      SceneModule.class,
      AttackModule.class,
      NotificationModule.class,
      RemoteGOModule.class,
      LocalGOModule.class,
      WindowsModule.class,
      ServerPositionModule.class,
      AttackNotifications.class
  );

  @Override
  protected ServerGuiceModule createGuiceModule() {
    return new ServerGuiceModule();
  }

  @Override
  public void setUp(FMLPreInitializationEvent event) {
    super.setUp(event);

    for (Class<? extends IModule> module : MODULES) {
      this.injector.getInstance(module).setUp();
    }
  }

  public void onServerStarted(FMLServerStartingEvent event) {
    for (Class<? extends IModule> module : MODULES) {
      this.injector.getInstance(module).onServerStart(event);
    }
  }
}
