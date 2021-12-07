package pw.tales.cofdsystem.mod.server.modules.attack;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;
import junit.framework.TestCase;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.action_attack.AttackAction;
import pw.tales.cofdsystem.action_attack.builder.AttackBuilder;
import pw.tales.cofdsystem.common.EnumSide;

public class AttackTest extends TestCase {

  private Attack attack;
  private AttackBuilder builder;
  private LocalDateTime time;
  private UUID uuid;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    this.uuid = UUID.randomUUID();
    this.builder = mock(AttackBuilder.class);
    this.time = mock(LocalDateTime.class);

    this.attack = new Attack(
        this.uuid,
        this.builder,
        this.time

    );
  }

  public void testExecute() {
    CofDSystem system = mock(CofDSystem.class);
    AttackAction action = mock(AttackAction.class);

    when(
        this.builder.build()
    ).thenReturn(action);

    this.attack.execute(system);

    verify(this.builder).build();
    verify(action).execute();
  }

  public void testGetWindowAttackerDn() {
    assertEquals(
        this.attack.getWindowDN(EnumSide.ACTOR),
        String.format("%s_attacker", this.uuid.toString())
    );
  }

  public void testGetWindowTargetDn() {
    assertEquals(
        this.attack.getWindowDN(EnumSide.TARGET),
        String.format("%s_target", this.uuid.toString())
    );
  }

  public void testBothConfirmedActorFirst() {
    this.methodTestBothConfirmed(
        EnumSide.ACTOR,
        EnumSide.TARGET
    );
  }

  public void methodTestBothConfirmed(EnumSide first, EnumSide second) {
    assertNotSame(first, second);
    assertFalse(this.attack.isBothConfirmed());
    attack.confirm(EnumSide.ACTOR);
    assertFalse(this.attack.isBothConfirmed());
    attack.confirm(EnumSide.TARGET);
    assertTrue(this.attack.isBothConfirmed());
  }

  public void testBothConfirmedTargetFirst() {
    this.methodTestBothConfirmed(
        EnumSide.TARGET,
        EnumSide.ACTOR
    );
  }

}