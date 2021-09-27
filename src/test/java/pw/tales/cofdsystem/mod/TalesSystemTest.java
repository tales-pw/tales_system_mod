package pw.tales.cofdsystem.mod;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import junit.framework.TestCase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.mockito.Mockito;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.server.modules.go_source.SourceGOModule;

public abstract class TalesSystemTest extends TestCase {

  protected CofDSystem system;
  protected FMLCommonHandler fmlCommonMock;
  protected SourceGOModule sourceMock;

  protected Map<UUID, EntityLivingBase> entities = new HashMap<>();
  protected Map<String, GameObject> gameObjects = new HashMap<>();

  public void setUp() throws Exception {
    super.setUp();
    this.system = new CofDSystem();

    this.setUpEntities();
    this.setUpRepository();
  }

  private void setUpRepository() {
    this.sourceMock = mock(SourceGOModule.class);

    for (int i = 0; i < 5; i++) {
      String dn = UUID.randomUUID().toString();
      GameObject gameObject = new GameObject(dn, this.system);

      gameObjects.put(dn, gameObject);

      when(
          this.sourceMock.getGameObject(dn)
      ).thenReturn(CompletableFuture.completedFuture(gameObject));
    }
  }

  private void setUpEntities() {
    this.fmlCommonMock = mock(FMLCommonHandler.class, Mockito.RETURNS_DEEP_STUBS);

    for (int i = 0; i < 5; i++) {
      UUID uuid = UUID.randomUUID();
      EntityLivingBase entity = mock(EntityLivingBase.class);

      when(entity.getPersistentID()).thenReturn(uuid);

      entities.put(uuid, entity);

      this.setUpEntity(entity);

      when(
          this.fmlCommonMock.getMinecraftServerInstance().getEntityFromUuid(uuid)
      ).thenReturn(entity);
    }
  }

  protected void setUpEntity(EntityLivingBase entity) {

  }

  public EntityLivingBase getTestEntity() {
    return this.entities.values().toArray(new EntityLivingBase[0])[0];
  }

  public GameObject getTestGameObject() {
    return this.gameObjects.values().toArray(new GameObject[0])[0];
  }
}