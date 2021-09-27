package pw.tales.cofdsystem.mod.server.modules.go_relation;

import java.util.concurrent.CompletableFuture;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.server.modules.go_source.IGOSource;

public abstract class GORelation<T> implements IGORelation<T> {

  private final IGOSource goSource;

  protected GORelation(IGOSource goSource) {
    this.goSource = goSource;
  }

  /**
   * Get GameObject for Entity.
   *
   * @param holder Entity to fetch GameObject for.
   * @return GameObject
   */
  @Override
  public CompletableFuture<GameObject> getGameObject(T holder) {
    String dn = this.getBind(holder);

    if (dn == null) {
      CompletableFuture<GameObject> excFuture = new CompletableFuture<>();
      excFuture.completeExceptionally(this.createNotBound(holder));
      return excFuture;
    }

    return this.goSource.getGameObject(dn);
  }

  protected abstract RuntimeException createNotBound(T holder);
}
