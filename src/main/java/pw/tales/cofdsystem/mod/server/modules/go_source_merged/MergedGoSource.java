package pw.tales.cofdsystem.mod.server.modules.go_source_merged;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.Utils;
import pw.tales.cofdsystem.mod.server.modules.go_source.IGOSource;
import pw.tales.cofdsystem.mod.server.modules.go_source.exceptions.GOFetchingException;
import pw.tales.cofdsystem.mod.server.modules.go_source_local.LocalGOModule;
import pw.tales.cofdsystem.mod.server.modules.go_source_remote.RemoteGOModule;

/**
 * Module responsible for fetching GameObject from multiple sources.
 * <p>
 * Pick first, if GameObject isn't there, pick second and so on.
 */
@Singleton
public class MergedGoSource implements IGOSource {

  private final List<IGOSource> sources = new ArrayList<>();

  @Inject
  public MergedGoSource(LocalGOModule localGOModule, RemoteGOModule remoteGOModule) {
    // TODO: Somehow get array.
    this.sources.add(localGOModule);
    this.sources.add(remoteGOModule);
  }

  @Override
  public CompletableFuture<GameObject> getGameObject(String dn) {
    for (IGOSource source : this.sources) {
      if (source.isLoaded(dn)) {
        return source.getGameObject(dn);
      }
    }

    CompletableFuture<GameObject> future = CompletableFuture.completedFuture(null);
    for (IGOSource source : sources) {
      future = future.thenCompose((gameObject -> {
        if (gameObject != null) {
          return CompletableFuture.completedFuture(gameObject);
        }
        return Utils.ignoreExc(
            source.getGameObject(dn),
            GOFetchingException.class
        );
      }));
    }

    return future.thenApply(gameObject -> {
      if (gameObject == null) {
        throw new GOFetchingException("GameObject not found in any sources.");
      }
      return gameObject;
    });
  }

  @Override
  public boolean isLoaded(String dn) {
    for (IGOSource source : sources) {
      if (source.isLoaded(dn)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void unload(String dn) {
    for (IGOSource source : sources) {
      source.unload(dn);
    }
  }
}
