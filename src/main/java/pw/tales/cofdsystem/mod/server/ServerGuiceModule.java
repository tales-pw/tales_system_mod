package pw.tales.cofdsystem.mod.server;

import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.mod.common.CommonGuiceModule;
import pw.tales.cofdsystem.mod.server.modules.attack.network.AttackMessageHandler;
import pw.tales.cofdsystem.mod.server.modules.equipment.network.handlers.TooltipRequestHandler;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.network.handlers.EntityGOBindHandler;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.network.handlers.EntityGOControlHandler;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.network.handlers.EntityGOUnloadHandler;
import pw.tales.cofdsystem.mod.server.modules.go_source.IGOSource;
import pw.tales.cofdsystem.mod.server.modules.go_source_merged.MergedGoSource;
import pw.tales.cofdsystem.mod.server.modules.position.network.handlers.GetRangeMessageHandler;
import pw.tales.cofdsystem.mod.server.modules.scene.network.ServerSceneAddHandler;
import pw.tales.cofdsystem.mod.server.modules.simple_roll.network.SimpleRollMessageHandler;
import pw.tales.cofdsystem.mod.server.views.View;

public class ServerGuiceModule extends CommonGuiceModule {

  public ServerGuiceModule(CofDSystem cofdSystem) {
    super(cofdSystem);
  }

  @Override
  protected void configure() {
    super.configure();
    bind(IGOSource.class).to(MergedGoSource.class);

    requestStaticInjection(ServerSceneAddHandler.class);
    requestStaticInjection(AttackMessageHandler.class);
    requestStaticInjection(SimpleRollMessageHandler.class);
    requestStaticInjection(EntityGOBindHandler.class);
    requestStaticInjection(EntityGOUnloadHandler.class);
    requestStaticInjection(EntityGOControlHandler.class);
    requestStaticInjection(TooltipRequestHandler.class);
    requestStaticInjection(GetRangeMessageHandler.class);
    requestStaticInjection(View.class);
  }
}
