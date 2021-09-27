package pw.tales.cofdsystem.mod.server.modules.scene.command;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import junit.framework.TestCase;
import net.minecraft.entity.player.EntityPlayer;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.GOEntityRelation;
import pw.tales.cofdsystem.mod.server.modules.operators.OperatorsModule;
import pw.tales.cofdsystem.mod.server.modules.scene.SceneModule;
import pw.tales.cofdsystem.scene.turns.Turns;

public class ServerSceneNextCommandTest extends TestCase {

  private ServerSceneNextCommand command;

  private GameObject gameObject;
  private EntityPlayer entity;
  private Turns turns;
  private OperatorsModule operatorsModule;

  @Override
  public void setUp() throws Exception {
    super.setUp();

    this.operatorsModule = mock(OperatorsModule.class);

    this.gameObject = mock(GameObject.class);
    this.entity = mock(EntityPlayer.class);
    this.turns = mock(Turns.class);

    this.command = new ServerSceneNextCommand(
        operatorsModule,
        mock(SceneModule.class),
        mock(GOEntityRelation.class)
    );
  }

  public void testCanNextNotAllowed() {
    this.setIsOperator(false);
    this.setCurrentTurn(mock(GameObject.class));

    assertFalse(
        this.command.canNext(this.entity, this.gameObject, this.turns)
    );
  }

  private void setIsOperator(boolean allowed) {
    when(
        this.operatorsModule.isOperator(this.entity)
    ).thenReturn(allowed);
  }

  private void setCurrentTurn(GameObject gameObject) {
    when(
        this.turns.getTurn()
    ).thenReturn(gameObject);
  }

  public void testCanNextAllowedCurrentTurn() {
    this.setIsOperator(false);
    this.setCurrentTurn(this.gameObject);

    assertTrue(
        this.command.canNext(this.entity, this.gameObject, this.turns)
    );
  }

  public void testCanNextAllowedHasPermission() {
    this.setIsOperator(true);
    this.setCurrentTurn(mock(GameObject.class));

    assertTrue(
        this.command.canNext(this.entity, this.gameObject, this.turns)
    );
  }

  public void testCanNextHasPermissionAndCurrentTurn() {
    this.setIsOperator(true);
    this.setCurrentTurn(this.gameObject);

    assertTrue(
        this.command.canNext(this.entity, this.gameObject, this.turns)
    );
  }
}