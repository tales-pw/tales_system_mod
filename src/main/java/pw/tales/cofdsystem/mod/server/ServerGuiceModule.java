package pw.tales.cofdsystem.mod.server;

import javax.inject.Provider;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.mod.ModConfig;
import pw.tales.cofdsystem.mod.common.CommonGuiceModule;
import pw.tales.cofdsystem.mod.common.errors.IErrorHandler;
import pw.tales.cofdsystem.mod.server.clients.AccountsClient;
import pw.tales.cofdsystem.mod.server.errors.ServerErrors;
import pw.tales.cofdsystem.mod.server.modules.attack.network.handlers.AttackMessageHandler;
import pw.tales.cofdsystem.mod.server.modules.attack.storage.AttackMapRepository;
import pw.tales.cofdsystem.mod.server.modules.attack.storage.IAttackRepository;
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
  @Override
  protected void configure() {
    super.configure();

    bind(IErrorHandler.class).to(ServerErrors.class);
    bind(IGOSource.class).to(MergedGoSource.class);
    bind(IAttackRepository.class).to(AttackMapRepository.class);
    bind(CofDSystem.class).toInstance(new CofDSystem());

    bind(AccountsClient.class).toProvider((Provider<AccountsClient>) () -> new AccountsClient(
        ModConfig.accountsApiUrl,
        ModConfig.accountsApiToken
    ));

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
