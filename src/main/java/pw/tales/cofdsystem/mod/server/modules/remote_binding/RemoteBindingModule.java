package pw.tales.cofdsystem.mod.server.modules.remote_binding;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.Utils;
import pw.tales.cofdsystem.mod.server.clients.accounts.AccountsClient;
import pw.tales.cofdsystem.mod.server.clients.accounts.exceptions.NotFound;
import pw.tales.cofdsystem.mod.server.errors.ServerErrors;
import pw.tales.cofdsystem.mod.server.modules.ServerCommandModule;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.GOEntityRelation;
import pw.tales.cofdsystem.mod.server.modules.go_source.IGOSource;
import pw.tales.cofdsystem.mod.server.modules.operators.OperatorsModule;
import pw.tales.cofdsystem.mod.server.modules.remote_binding.command.ForceRemoteBindingCommand;

@Singleton
public class RemoteBindingModule extends ServerCommandModule {

  private final AccountsClient accountsClient;
  private final IGOSource source;
  private final OperatorsModule operatorsModule;
  private final GOEntityRelation relationModule;
  private final ServerErrors serverErrors;

  @Inject
  public RemoteBindingModule(
      AccountsClient accountsClient,
      IGOSource source,
      OperatorsModule operatorsModule,
      GOEntityRelation relationModule,
      ServerErrors serverErrors,
      Injector injector
  ) {
    super(injector);
    this.accountsClient = accountsClient;
    this.source = source;
    this.operatorsModule = operatorsModule;
    this.relationModule = relationModule;
    this.serverErrors = serverErrors;
  }

  @Override
  public void setUp() {
    EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void onPlayerJoin(PlayerLoggedInEvent event) {
    EntityPlayer player = event.player;

    // Don't autobind already bound character
    String bind = this.relationModule.getBind(player);
    if (!Objects.equals(bind, null)) {
      return;
    }

    Utils.ignoreExc(
        this.applyRemoteBinding(player), NotFound.class
    ).exceptionally(e -> {
      RemoteBindingModule.this.serverErrors.handle(player, e);
      throw new CompletionException(e);
    });
  }

  public CompletableFuture<GameObject> applyRemoteBinding(EntityPlayer player) {
    String username = player.getName();

    TalesSystem.logger.info("Loading binding for {}.", username);

    return this.accountsClient.getBindingByUsername(username).thenCompose(
        binding -> this.source.getGameObject(binding.dn)
    ).thenApply(gameObject -> {
      this.relationModule.bind(player, gameObject);
      TalesSystem.logger.info(
          "Remote binding ({}) applied for {}.",
          gameObject.getDN(),
          username
      );
      return gameObject;
    }).exceptionally(e -> {
      if (e instanceof NotFound) {
        TalesSystem.logger.info(
            "Binding for {} not found.",
            username
        );
      } else {
        TalesSystem.logger.error("Error while setting remote binding for.", e);
      }

      throw new CompletionException(e);
    });
  }

  @Override
  public Set<Class<? extends CommandBase>> getCommandClasses() {
    return ImmutableSet.of(ForceRemoteBindingCommand.class);
  }
}
