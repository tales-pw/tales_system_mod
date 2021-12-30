package pw.tales.cofdsystem.mod.server.modules.player;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Objects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import pw.tales.cofdsystem.mod.common.IModule;
import pw.tales.cofdsystem.mod.server.clients.AccountsClient;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.GOEntityRelation;
import pw.tales.cofdsystem.mod.server.modules.go_source.IGOSource;

@Singleton
public class PlayerModule implements IModule {

  private final AccountsClient accountsClient;
  private final IGOSource source;
  private final GOEntityRelation relationModule;

  @Inject
  public PlayerModule(
      AccountsClient accountsClient,
      IGOSource source,
      GOEntityRelation relationModule
  ) {
    this.accountsClient = accountsClient;
    this.source = source;
    this.relationModule = relationModule;
  }

  @Override
  public void setUp() {
    EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void onPlayerJoin(PlayerLoggedInEvent event) {
    EntityPlayer player = event.player;

    String bind = this.relationModule.getBind(player);
    if (!Objects.equals(bind, null)) {
      return;
    }

    this.accountsClient.get_binding_by_username(player.getName()).thenCompose(
        binding -> this.source.getGameObject(binding.dn)
    ).thenAccept(
        gameObject -> this.relationModule.bind(player, gameObject)
    );
  }
}
