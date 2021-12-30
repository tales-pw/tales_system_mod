package pw.tales.cofdsystem.mod.server.modules.go_source_remote;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import haxe.lang.Function;
import haxe.root.Array;
import java.util.concurrent.CompletableFuture;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.game_object.traits.Trait;
import pw.tales.cofdsystem.mod.ModConfig;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.common.haxe_adapters.HaxeArrayAdapter;
import pw.tales.cofdsystem.mod.common.haxe_adapters.HaxeFn;
import pw.tales.cofdsystem.mod.server.modules.go_source.SourceGOModule;
import pw.tales.cofdsystem.mod.server.modules.go_source.exceptions.GOFetchingException;
import pw.tales.cofdsystem.synchronization.rest.GameObjectStorage;

/**
 * Module responsible for fetching GameObject from remote storage.
 */
@Singleton
public class RemoteGOModule extends SourceGOModule {

  @Inject
  public RemoteGOModule(CofDSystem cofdsystem) {
    super(cofdsystem);
  }

  /**
   * Fetch GameObject from remote server without saving it anywhere.
   *
   * @param dn GameObject dn.
   * @return CompletableFuture storing GameObject result or error.
   */
  public CompletableFuture<GameObject> fetch(String dn) {
    CompletableFuture<GameObject> future = new CompletableFuture<>();

    GameObjectStorage storage = this.createGameObjectStorage();
    storage.onGameObject = HaxeFn.wrap((GameObject gameObject) -> {
      TalesSystem.logger.info(
          "{} loaded from {}.",
          gameObject,
          RemoteGOModule.this
      );
      future.complete(gameObject);
    });

    storage.onError = new Function(2, 2) {
      @Override
      public Object __hx_invoke2_o(double v, Object o, double v1, Object o1) {
        TalesSystem.logger.error(
            "Unable to load {} from {}: {}.",
            dn,
            RemoteGOModule.this,
            o
        );
        future.completeExceptionally(new GOFetchingException(
            String.format("Can't get GameObject from %s, because %s",
                RemoteGOModule.this,
                o
            ),
            dn));
        return null;
      }
    };

    storage.read(dn);
    return future;
  }

  /**
   * Create client class.
   */
  private GameObjectStorage createGameObjectStorage() {
    return GameObjectStorage
        .createForServer(ModConfig.systemApiUrl, cofdsystem, ModConfig.systemApiToken);
  }

  /**
   * Update GameObject on remote storage.
   *
   * @param gameObject GameObject.
   * @param update     Traits to update.
   * @param remove     Traits to remove.
   */
  public void save(GameObject gameObject, Array<Trait> update, Array<Trait> remove) {
    GameObjectStorage storage = this.createGameObjectStorage();

    Array<String> removeDNs = new Array<>();
    for (Trait trait : new HaxeArrayAdapter<>(remove)) {
      removeDNs.push(trait.getDN());
    }

    storage.onUpdated = new Function(1, 0) {
      @Override
      public Object __hx_invoke1_o(double v, Object o) {
        TalesSystem.logger.info(
            "{} updated to {}: {}.",
            gameObject.getDN(),
            RemoteGOModule.this,
            gameObject.version
        );
        return null;
      }
    };

    storage.onError = new Function(2, 2) {
      @Override
      public Object __hx_invoke2_o(double v, Object o, double v1, Object o1) {
        TalesSystem.logger.error(
            "Unable to update {} to {}: {}.",
            gameObject.getDN(),
            RemoteGOModule.this,
            o
        );
        return null;
      }
    };

    storage.update(gameObject, update, removeDNs);
  }
}
