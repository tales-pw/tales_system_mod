package pw.tales.cofdsystem.mod.server.modules.go_source_local;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import haxe.root.Array;
import java.util.concurrent.CompletableFuture;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.game_object.traits.Trait;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.server.modules.go_source.SourceGOModule;
import pw.tales.cofdsystem.mod.server.modules.go_source.exceptions.GOFetchingException;

/**
 * Module responsible for fetching GameObject from local storage (MapStorage).
 */
@Singleton
public class LocalGOModule extends SourceGOModule {

  private final FMLCommonHandler fmlCommonHandler;

  @Inject
  public LocalGOModule(CofDSystem cofdsystem, FMLCommonHandler fmlCommonHandler) {
    super(cofdsystem);
    this.fmlCommonHandler = fmlCommonHandler;
    LocalGOWorldData.cofdsystem = cofdsystem;
  }

  public CompletableFuture<GameObject> fetch(String dn) {
    MapStorage storage = this.getMapStorage();
    LocalGOWorldData data = LocalGOWorldData.get(storage, dn);

    if (data == null || data.getGameObject() == null) {
      TalesSystem.logger.info("{} missing in {}.", dn, this);

      CompletableFuture<GameObject> future = new CompletableFuture<>();
      future.completeExceptionally(
          new GOFetchingException(
              String.format(
                  "Not found in %s",
                  this
              ),
              dn)
      );
      return future;
    }

    GameObject gameObject = data.getGameObject();
    TalesSystem.logger.info("{} loaded from {}.", gameObject, this);
    return CompletableFuture.completedFuture(gameObject);
  }

  /**
   * Get MapStorage.
   */
  private MapStorage getMapStorage() {
    return this.fmlCommonHandler.getMinecraftServerInstance().getEntityWorld().getMapStorage();
  }

  public void save(GameObject gameObject) {
    this.save(gameObject, new Array<>(), new Array<>());
  }

  @Override
  public void save(GameObject gameObject, Array<Trait> update, Array<Trait> remove) {
    MapStorage storage = getMapStorage();
    LocalGOWorldData.save(storage, gameObject);
    TalesSystem.logger.info(
        "{} updated in {}: {}.",
        gameObject.getDN(),
        this,
        gameObject.version
    );
  }

  public GameObject cloneGameObject(GameObject gameObject) {
    GameObject clone = gameObject.clone();
    this.save(clone);
    TalesSystem.logger.info(
        "{}, clone of {}, create and saved to {}",
        clone,
        gameObject,
        this
    );
    return clone;
  }
}
