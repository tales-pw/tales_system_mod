package pw.tales.cofdsystem.mod.server.modules.attack.storage;

import static org.mockito.Mockito.mock;

import junit.framework.TestCase;
import pw.tales.cofdsystem.action_attack.builder.AttackBuilder;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.server.modules.attack.Attack;

public class AttackMapRepositoryTest extends TestCase {

  private AttackMapRepository repository;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    this.repository = new AttackMapRepository();
  }

  public void testDeleteInGetAll() {
    GameObject go1 = mock(GameObject.class);
    GameObject go2 = mock(GameObject.class);

    this.repository.save(new AttackBuilder(go1, go2));
    this.repository.save(new AttackBuilder(go1, go2));

    assertEquals(this.repository.getAll().size(), 2);
    for (Attack attack : this.repository.getAll()) {
      this.repository.remove(attack);
    }
    assertEquals(this.repository.getAll().size(), 0);
  }
}