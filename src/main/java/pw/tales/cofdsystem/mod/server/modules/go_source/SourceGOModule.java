package pw.tales.cofdsystem.mod.server.modules.go_source;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.google.inject.Inject;
import haxe.root.Array;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.game_object.TraitTracker;
import pw.tales.cofdsystem.game_object.events.tracker.TrackerTrackEvent;
import pw.tales.cofdsystem.game_object.traits.Trait;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.common.IModule;
import pw.tales.cofdsystem.mod.common.haxe_adapters.HaxeFn;
import pw.tales.cofdsystem.mod.server.modules.go_source.events.GameObjectLoadedEvent;
import pw.tales.cofdsystem.mod.server.modules.go_source.events.GameObjectUnloadingEvent;
import pw.tales.cofdsystem.utils.events.HandlerPriority;
import pw.tales.cofdsystem.utils.registry.Registry;

/**
 * Base class for GameObject sources.
 */
public abstract class SourceGOModule implements IModule, IGOSource {

  protected final CofDSystem cofdsystem;
  protected final Registry<GameObject> registry = new Registry<>(null, null);

  private final Set<TraitTracker> trackers = new HashSet<>();

  @Inject
  protected SourceGOModule(CofDSystem cofdsystem) {
    this.cofdsystem = cofdsystem;
  }

  @Override
  public void setUp() {
    this.cofdsystem.events.addHandler(TrackerTrackEvent.class, HaxeFn.wrap(
        this::onTrackerEvent
    ), HandlerPriority.LOWEST);
  }

  /**
   * Persist game object change in service
   *
   * @param event Event.
   */
  public void onTrackerEvent(TrackerTrackEvent event) {
    TraitTracker tracker = event.getTracker();

    if (!this.trackers.contains(tracker)) {
      return;
    }

    if (!tracker.hasChanges()) {
      return;
    }

    GameObject gameObject = tracker.getGameObject();

    Array<Trait> update = tracker.getUpdate().copy();
    Array<Trait> remove = tracker.getRemove().copy();
    tracker.clear();

    TalesSystem.logger.info("Updating {}, {}: {}", gameObject, update, tracker.getRemove());
    this.save(gameObject, update, remove);
  }

  public abstract void save(GameObject gameObject, Array<Trait> update, Array<Trait> remove);

  public CompletableFuture<GameObject> getGameObject(String dn) {
    // Check if GameObject is already loaded
    GameObject loadedGameObject = registry.getRecord(dn);

    if (loadedGameObject != null) {
      // GameObject already loaded, just use it
      return CompletableFuture.completedFuture(loadedGameObject);
    }

    // Load GameObject for remote source
    return this.loadGameObject(dn);
  }

  /**
   * Load GameObject. Should be called only once, otherwise exception will be thrown.
   *
   * @param dn DN for which to load GameObject.
   * @return CompletableFuture that store GameObject.
   */
  public CompletableFuture<GameObject> loadGameObject(String dn) {
    return this.fetch(dn).thenApply((gameObject -> {
      this.registry.register(gameObject);
      trackers.add(new TraitTracker(gameObject));
      EVENT_BUS.post(new GameObjectLoadedEvent(gameObject));
      return gameObject;
    }));
  }

  public abstract CompletableFuture<GameObject> fetch(String dn);

  /**
   * Unload GameObject with specified dn.
   *
   * @param dn GameObject's DN.
   */
  @Override
  public void unload(String dn) {
    GameObject gameObject = this.registry.getRecord(dn);
    if (gameObject != null) {
      EVENT_BUS.post(new GameObjectUnloadingEvent.PRE(gameObject));
      this.registry.unregister(gameObject);
      gameObject.deactivate();
      EVENT_BUS.post(new GameObjectUnloadingEvent.POST(gameObject));
      TalesSystem.logger.info(
          "{} unloaded in {}",
          gameObject,
          this
      );
    }
  }

  @Override
  public boolean isLoaded(String dn) {
    return this.registry.getRecord(dn) != null;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
}
