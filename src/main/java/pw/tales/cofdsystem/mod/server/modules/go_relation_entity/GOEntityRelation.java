package pw.tales.cofdsystem.mod.server.modules.go_relation_entity;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.common.IModule;
import pw.tales.cofdsystem.mod.server.modules.go_relation.GORelation;
import pw.tales.cofdsystem.mod.server.modules.go_relation.exceptions.NotBoundException;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.capabilities.binding.GOBindingHolder;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.capabilities.binding.GOBindingProvider;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.events.GameObjectAttachedEvent;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.events.GameObjectDetachedEvent;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.exceptions.EntityNotBoundException;
import pw.tales.cofdsystem.mod.server.modules.go_source.IGOSource;

/**
 * Module responsible for binding GameObjects to Entities.
 */
@Singleton
public class GOEntityRelation extends GORelation<Entity> implements IModule {

  private final IGOSource goSource;
  private final FMLCommonHandler fmlCommonHandler;

  private final Map<GameObject, Set<UUID>> attachment = new HashMap<>();
  private final Map<Entity, Entity> control = new HashMap<>();

  @Inject
  public GOEntityRelation(IGOSource goSource, FMLCommonHandler fmlCommonHandler) {
    super(goSource);
    this.goSource = goSource;
    this.fmlCommonHandler = fmlCommonHandler;
  }

  @Override
  public void setUp() {
    GOBindingHolder.register();
    EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
    event.addCapability(
        new ResourceLocation(TalesSystem.MOD_ID, "game_object"),
        new GOBindingProvider()
    );
  }

  /**
   * Get GameObject for Entity.
   *
   * @param holder Entity to fetch GameObject for.
   * @return GameObject.
   */
  @Override
  public CompletableFuture<GameObject> getGameObject(Entity holder) {
    return this.getGameObject(holder, true);
  }

  /**
   * Get GameObject for Entity.
   *
   * @param holder   Entity to fetch GameObject for.
   * @param onlyFull Flag if GameObject requested for fully bound character or may be used for
   *                 somebody who is controlling character (like ST controlling NPC).
   * @return GameObject.
   */
  public CompletableFuture<GameObject> getGameObject(Entity holder, boolean onlyFull) {
    if (!onlyFull) {
      holder = control.getOrDefault(holder, holder);
    }

    Entity finalHolder = holder;
    return super.getGameObject(finalHolder).thenApply(gameObject -> {
      this.attach(finalHolder, gameObject);
      return gameObject;
    });
  }

  /**
   * Add entity to "GameObject to Entity"-mapping.
   *
   * @param entity     Entity.
   * @param gameObject GameObject.
   */
  public void attach(Entity entity, GameObject gameObject) {
    Set<UUID> entities = this.getEntityUUIDs(gameObject);
    UUID persistentID = entity.getPersistentID();

    // Check is needed to not post event when gameObject already attached.
    if (entities.contains(persistentID)) {
      return;
    }

    entities.add(persistentID);
    EVENT_BUS.post(new GameObjectAttachedEvent(entity, gameObject));
  }

  /**
   * Get entity UUIDs for GameObject.
   *
   * @param gameObject GameObject.
   * @return Set of UUIDs.
   */
  private Set<UUID> getEntityUUIDs(GameObject gameObject) {
    Set<UUID> entities;
    if (attachment.containsKey(gameObject)) {
      entities = attachment.get(gameObject);
    } else {
      entities = new HashSet<>();
      attachment.put(gameObject, entities);
    }
    return entities;
  }

  /**
   * Bind GameObject to Entity.
   *
   * @param entity     Entity.
   * @param gameObject GameObject.
   */
  @Override
  public void bind(Entity entity, @Nullable GameObject gameObject) {
    GOBindingHolder capability = entity.getCapability(
        GOBindingProvider.GO_BINDING_CAPABILITY, null
    );

    if (capability == null) {
      return;
    }

    if (gameObject == null) {
      capability.setDN(null);
    } else {
      capability.setDN(gameObject.getDN());
    }
  }

  /**
   * Allow entity to control other entity's system actions.
   *
   * @param controller Entity who controls.
   * @param toControl  Entity to control.
   */
  public void control(Entity controller, @Nullable Entity toControl) {
    if (toControl == null) {
      this.control.remove(controller);
    } else {
      this.control.put(controller, toControl);
    }
  }

  /**
   * Get GameObject for DN.
   *
   * @param dn DN.
   * @return GameObject.
   */
  public CompletableFuture<GameObject> getGameObject(String dn) {
    return this.goSource.getGameObject(dn);
  }

  /**
   * Remove entity to "GameObject to Entity"-mapping.
   *
   * @param entity     Entity.
   * @param gameObject GameObject.
   */
  public void detach(Entity entity, GameObject gameObject) {
    Set<UUID> entities = this.getEntityUUIDs(gameObject);
    UUID persistentID = entity.getPersistentID();

    // Check is needed to not post event when gameObject already detached.
    if (!entities.contains(persistentID)) {
      return;
    }

    entities.remove(persistentID);
    EVENT_BUS.post(new GameObjectDetachedEvent(entity, gameObject));
  }

  public Set<Entity> getEntities(GameObject gameObject) {
    return this.getEntities(gameObject, Entity.class);
  }

  /**
   * Get Entity for GameObject
   *
   * @param gameObject GameObject for which to get Entity
   * @return Entity
   */
  public <T extends Entity> Set<T> getEntities(GameObject gameObject, Class<T> clazz) {
    Set<UUID> uuids = attachment.getOrDefault(gameObject, null);
    if (uuids == null) {
      return Collections.emptySet();
    }

    MinecraftServer server = this.fmlCommonHandler.getMinecraftServerInstance();

    return uuids.stream()
        .map(server::getEntityFromUuid)
        .filter(Objects::nonNull)
        .filter(e -> clazz.isAssignableFrom(e.getClass()))
        .map(clazz::cast)
        .collect(Collectors.toSet());
  }

  /**
   * Unload GameObject bound to Entity.
   *
   * @param entity Entity.
   */
  public void unload(Entity entity) {
    String dn = this.getBind(entity);

    if (dn == null) {
      throw this.createNotBound(entity);
    }

    if (this.goSource.isLoaded(dn)) {
      this.goSource.unload(dn);
    }
  }

  @Override
  protected NotBoundException createNotBound(Entity holder) {
    return new EntityNotBoundException(holder);
  }

  /**
   * Get GameObject DN bound to Entity.
   *
   * @param entity Entity.
   * @return DN.
   */
  @Override
  @Nullable
  public String getBind(Entity entity) {
    GOBindingHolder capability = entity.getCapability(
        GOBindingProvider.GO_BINDING_CAPABILITY, null
    );

    if (capability == null || capability.getDN() == null) {
      return null;
    }

    return capability.getDN();
  }

  public boolean isBound(Entity entity) {
    return this.getBind(entity) != null;
  }
}
