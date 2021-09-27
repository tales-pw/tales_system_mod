package pw.tales.cofdsystem.mod.server.modules.go_source_local;

import net.minecraft.nbt.NBTTagCompound;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.TalesSystemTest;
import pw.tales.cofdsystem.synchronization.GameObjectSynchronization;

public class LocalGOWorldSavedDataTest extends TalesSystemTest {

  private LocalGOWorldData storage;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    LocalGOWorldData.cofdsystem = this.system;
    this.storage = new LocalGOWorldData("name");
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    LocalGOWorldData.cofdsystem = null;
  }

  public void testWrite() {
    GameObject gameObject = this.getTestGameObject();

    this.storage.setGameObject(gameObject);
    NBTTagCompound result = this.storage.writeToNBT(new NBTTagCompound());

    assertNotNull(result);
    assertEquals(result.getString("dn"), gameObject.getDN());
    assertEquals(result.getString("data"), this.serialize(gameObject));
  }

  private String serialize(GameObject gameObject) {
    GameObjectSynchronization sync = GameObjectSynchronization.create(
        this.system,
        gameObject
    );
    return sync.serialize();
  }

  public void testRecreateFromNBT() {
    GameObject gameObject = this.getTestGameObject();
    String serialized = this.serialize(gameObject);

    NBTTagCompound tag = new NBTTagCompound();
    tag.setString("dn", gameObject.getDN());
    tag.setString("data", serialized);

    this.storage.readFromNBT(tag);

    GameObject fetchedGameObject = this.storage.getGameObject();
    String serializedFetched = this.serialize(fetchedGameObject);

    assertEquals(gameObject.getDN(), fetchedGameObject.getDN());
    assertEquals(serialized, serializedFetched);
  }

  public void testReadWritten() {
    GameObject gameObject = this.getTestGameObject();
    String serialized = this.serialize(gameObject);
    this.storage.setGameObject(gameObject);

    NBTTagCompound tag = this.storage.writeToNBT(new NBTTagCompound());
    this.storage.readFromNBT(tag);

    GameObject fetchedGameObject = this.storage.getGameObject();
    String serializedFetched = this.serialize(fetchedGameObject);

    assertEquals(gameObject.getDN(), fetchedGameObject.getDN());
    assertEquals(serialized, serializedFetched);
  }
}