package pw.tales.cofdsystem.mod.server.modules.go_source;

import java.util.concurrent.CompletableFuture;
import pw.tales.cofdsystem.game_object.GameObject;

public interface IGOSource {

  CompletableFuture<GameObject> getGameObject(String dn);

  boolean isLoaded(String dn);

  void unload(String dn);
}
