package pw.tales.cofdsystem.mod.server.modules.scene;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.common.haxe_adapters.HaxeArrayAdapter;
import pw.tales.cofdsystem.mod.common.haxe_adapters.HaxeFn;
import pw.tales.cofdsystem.mod.server.modules.ServerCommandModule;
import pw.tales.cofdsystem.mod.server.modules.go_source.events.GameObjectUnloadingEvent;
import pw.tales.cofdsystem.mod.server.modules.gui_windows.WindowsModule;
import pw.tales.cofdsystem.mod.server.modules.notification.NotificationModule;
import pw.tales.cofdsystem.mod.server.modules.scene.command.ServerSceneBeginCommand;
import pw.tales.cofdsystem.mod.server.modules.scene.command.ServerSceneBindCommand;
import pw.tales.cofdsystem.mod.server.modules.scene.command.ServerSceneEndCommand;
import pw.tales.cofdsystem.mod.server.modules.scene.command.ServerSceneJoinCommand;
import pw.tales.cofdsystem.mod.server.modules.scene.command.ServerSceneListCommand;
import pw.tales.cofdsystem.mod.server.modules.scene.command.ServerSceneMenuCommand;
import pw.tales.cofdsystem.mod.server.modules.scene.command.ServerSceneNewCommand;
import pw.tales.cofdsystem.mod.server.modules.scene.command.ServerSceneNextCommand;
import pw.tales.cofdsystem.mod.server.modules.scene.command.ServerSceneShowCommand;
import pw.tales.cofdsystem.mod.server.modules.scene.views.SceneMenuView;
import pw.tales.cofdsystem.mod.server.modules.scene.views.TurnsMenuView;
import pw.tales.cofdsystem.mod.server.views.View;
import pw.tales.cofdsystem.scene.Scene;
import pw.tales.cofdsystem.scene.initiative.events.InitiativeUpdateEvent;
import pw.tales.cofdsystem.scene.turns.events.TurnStartEvent;
import pw.tales.cofdsystem.utils.events.HandlerPriority;
import pw.tales.cofdsystem.utils.registry.Registry;

@Singleton
public class SceneModule extends ServerCommandModule {

  public static final String SCENE_WINDOW_DN = "scene_window_dn";

  private final CofDSystem cofdSystem;
  private final NotificationModule notificationModule;
  private final WindowsModule windowsModule;

  private final Registry<Scene> scenes = new Registry<>(false, false);
  private final HashMap<ServerPlayerEntity, Scene> mapping = new HashMap<>();

  @Inject
  public SceneModule(
      CofDSystem system,
      NotificationModule notificationModule,
      WindowsModule windowsModule,
      Injector injector
  ) {
    super(injector);
    this.cofdSystem = system;
    this.notificationModule = notificationModule;
    this.windowsModule = windowsModule;
  }

  /**
   * @param scene Scene.
   * @return List of GameObject participating in scene.
   */
  public static List<GameObject> getParticipants(Scene scene) {
    return new HaxeArrayAdapter<>(scene.getInitiative().getOrder());
  }

  /**
   * Update main scene window. Sets view for main scene view slot.
   *
   * @param view      Window view builder.
   * @param entity    Entity.
   * @param forceOpen Should window be forced to open.
   */
  public void updateSceneWindow(View view, ServerPlayerEntity entity, boolean forceOpen) {
    this.windowsModule.updateWindow(entity, view, SCENE_WINDOW_DN, forceOpen);
  }

  /**
   * Update menu window for entity.
   *
   * @param entity Entity.
   */
  public void updateSceneMenuWindow(ServerPlayerEntity entity) {
    Scene scene = this.getBoundScene(entity);
    SceneMenuView view = new SceneMenuView(scene);
    this.updateSceneWindow(view, entity, true);
  }

  /**
   * @return Commands to register.
   */
  @Override
  public Set<Class<? extends CommandBase>> getCommandClasses() {
    return ImmutableSet.of(
        ServerSceneBeginCommand.class,
        ServerSceneMenuCommand.class,
        ServerSceneListCommand.class,
        ServerSceneBindCommand.class,
        ServerSceneNewCommand.class,
        ServerSceneJoinCommand.class,
        ServerSceneShowCommand.class,
        ServerSceneBeginCommand.class,
        ServerSceneNextCommand.class,
        ServerSceneEndCommand.class
    );
  }

  /**
   * Remove GameObject from all scenes when it is unloaded.
   *
   * @param event GameObjectUnloadingEvent.
   */
  @SubscribeEvent
  public void onGameObjectUnloading(GameObjectUnloadingEvent.PRE event) {
    for (Scene scene : new HaxeArrayAdapter<>(this.scenes.items())) {
      scene.remove(event.getGameObject());
    }
  }

  /**
   * Register event handlers on game start.
   */
  @Override
  public void setUp() {
    EVENT_BUS.register(this);

    cofdSystem.events.addHandler(TurnStartEvent.class, HaxeFn.wrap(
        this::onNewTurn
    ), HandlerPriority.NORMAL);

    cofdSystem.events.addHandler(InitiativeUpdateEvent.class, HaxeFn.wrap(
        this::onInitiativeUpdate
    ), HandlerPriority.NORMAL);
  }

  /**
   * Update windows with turns for game object.
   *
   * @param scene      Scene.
   * @param gameObject GameObject.
   * @return List of entities that received window update.
   */
  @Nullable
  public ServerPlayerEntity updateTurnWindow(
      Scene scene,
      GameObject gameObject,
      boolean forcedUpdate
  ) {
    return this.notificationModule.updateGoWindow(
        new TurnsMenuView(scene, gameObject),
        scene.getDN(),
        gameObject,
        forcedUpdate
    );
  }

  /**
   * Update windows with turns for all entities bound or participating in scene.
   *
   * @param scene Scene.
   */
  public void updateTurnWindow(Scene scene) {
    Set<ServerPlayerEntity> turnBoundEntities = this.getBoundEntities(scene);

    // Fetch entities that participate in fight
    // while removing them from bound list
    SceneModule.getParticipants(scene).stream().map(
        gameObject -> this.updateTurnWindow(scene, gameObject, false)
    ).forEach(turnBoundEntities::remove);

    // Send to bound entities
    TurnsMenuView view = new TurnsMenuView(scene, null);
    String sceneDN = scene.getDN();
    turnBoundEntities.forEach(
        entity -> this.windowsModule.updateWindow(
            entity,
            view,
            sceneDN,
            false
        )
    );
  }

  /**
   * Update all needed menus when new turn starts.
   *
   * @param event TurnStartEvent.
   */
  public void onNewTurn(TurnStartEvent event) {
    Scene scene = event.getInitiative().getScene();
    this.updateTurnWindow(scene);
  }

  /**
   * Update all needed menus when initiative list changes (new character - etc).
   *
   * @param event InitiativeUpdateEvent.
   */
  public void onInitiativeUpdate(InitiativeUpdateEvent event) {
    Scene scene = event.getInitiative().scene;
    this.updateTurnWindow(scene);
    this.updateSceneMenuWindow(scene);
  }

  /**
   * Update scene menu windows for all entities bound to scene.
   *
   * @param scene Scene.
   */
  private void updateSceneMenuWindow(Scene scene) {
    this.getBoundEntities(scene).forEach(this::updateSceneMenuWindow);
  }

  /**
   * Unbound all entities from scene.
   *
   * @param scene Scene.
   * @return Entities unbound from scene.
   */
  public Set<ServerPlayerEntity> unbindScene(Scene scene) {
    Set<ServerPlayerEntity> entities = this.getBoundEntities(scene);
    entities.forEach(mapping::remove);
    return entities;
  }

  /**
   * @param scene Scene.
   * @return List of all entities bound to scene.
   */
  public Set<ServerPlayerEntity> getBoundEntities(Scene scene) {
    return this.mapping.entrySet().stream()
        .filter(e -> e.getValue() == scene)
        .map(Entry::getKey)
        .collect(Collectors.toSet());
  }

  /**
   * @param entity Entity.
   * @return Scene bound to entity (null if not bound).
   */
  @Nullable
  public Scene getBoundScene(ServerPlayerEntity entity) {
    return this.mapping.getOrDefault(entity, null);
  }

  /**
   * Bind scene to entity. If Entity is bound to scene, it means that it receives all notifications
   * for it and can edit it from menus (if operator).
   *
   * @param entity Entity.
   * @param scene  Scene.
   */
  public void bindScene(ServerPlayerEntity entity, Scene scene) {
    this.mapping.put(entity, scene);
  }

  /**
   * @return Scene registry.
   */
  public Registry<Scene> getSceneRegistry() {
    return scenes;
  }
}
