package pw.tales.cofdsystem.mod.server.modules.go_relation;

import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
import pw.tales.cofdsystem.game_object.GameObject;

public interface IGORelation<T> {

  CompletableFuture<GameObject> getGameObject(T holder);

  @Nullable
  String getBind(T holder);

  void bind(T holder, @Nullable GameObject gameObject);
}
