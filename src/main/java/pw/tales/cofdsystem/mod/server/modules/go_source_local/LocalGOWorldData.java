package pw.tales.cofdsystem.mod.server.modules.go_source_local;

import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.synchronization.serialization.game_object.GameObjectSerialization;


/**
 * WorldSavedData implementation for GameObjects.
 */
public class LocalGOWorldData extends WorldSavedData {

  public static CofDSystem cofdsystem = null;
  private final GameObjectSerialization serialization;

  private GameObject gameObject = null;

  public LocalGOWorldData(String name) {
    super(name);

    // To make sure cofdsystem is set from outside.
    // We have to do this because there is no way to
    // set cofdsystem through constructor (it is often called
    // from inside of minecraft).
    if (cofdsystem == null) {
      throw new RuntimeException("cofdsystem is not set");
    }

    this.serialization = new GameObjectSerialization(cofdsystem);
  }

  public static LocalGOWorldData save(MapStorage storage, GameObject gameObject) {
    String key = getKey(gameObject);

    LocalGOWorldData data = get(storage, gameObject.getDN());

    if (data == null) {
      data = new LocalGOWorldData(key);
      data.setGameObject(gameObject);
    }

    data.markDirty();
    storage.setData(key, data);

    return data;
  }

  public static String getKey(GameObject gameObject) {
    return getKey(gameObject.getDN());
  }

  public static String getKey(String dn) {
    return "lgo_" + dn;
  }

  @Nullable
  public static LocalGOWorldData get(MapStorage storage, String dn) {
    String key = getKey(dn);
    return (LocalGOWorldData) storage.getOrLoadData(
        LocalGOWorldData.class,
        key
    );
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    String dn = tag.getString("dn");
    if (dn.isEmpty()) {
      return;
    }

    String data = tag.getString("data");
    GameObject go = this.serialization.deserialize(data);
    this.setGameObject(go);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag) {
    GameObject go = this.getGameObject();

    if (go == null) {
      return tag;
    }

    tag.setString("dn", gameObject.getDN());
    tag.setString("data", this.serialization.serialize(go));

    return tag;
  }

  @Nullable
  public GameObject getGameObject() {
    return gameObject;
  }

  public void setGameObject(@Nullable GameObject gameObject) {
    this.gameObject = gameObject;
  }
}
