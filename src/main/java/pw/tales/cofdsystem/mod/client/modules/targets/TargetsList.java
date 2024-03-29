package pw.tales.cofdsystem.mod.client.modules.targets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

@Singleton
public class TargetsList {

  List<UUID> uuid = new ArrayList<>();

  @Inject
  public TargetsList() {

  }

  public void addEntity(Entity entity) {
    this.uuid.add(entity.getPersistentID());
  }

  public void removeEntity(Entity entity) {
    this.uuid.remove(entity.getPersistentID());
  }

  public void clear() {
    this.uuid.clear();
  }

  public List<UUID> getUUIDs() {
    return this.uuid;
  }

  /**
   * Get target entities skipping unloaded ones.
   */
  public Entity[] getLoadedEntities() {
    return Arrays.stream(this.getEntities())
        .filter(o -> !Objects.isNull(o))
        .toArray(Entity[]::new);
  }

  /**
   * Get target entities with unloaded ones represented by null.
   */
  public Entity[] getEntities() {
    if (this.uuid.isEmpty()) {
      return new Entity[0];
    }

    List<Entity> entities = new ArrayList<>();

    for (Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
      if (this.uuid.contains(entity.getPersistentID())) {
        entities.add(entity);
      } else {
        entities.add(null);
      }
    }

    return entities.toArray(new Entity[0]);
  }

  public boolean hasEntity(Entity entity) {
    return this.uuid.contains(entity.getPersistentID());
  }
}
