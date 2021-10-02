package pw.tales.cofdsystem.mod.server.modules.attack;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.Utils;
import pw.tales.cofdsystem.mod.server.modules.ServerCommandModule;
import pw.tales.cofdsystem.mod.server.modules.attack.command.AttackShowCommand;
import pw.tales.cofdsystem.mod.server.modules.attack.command.ConfigureActorCommand;
import pw.tales.cofdsystem.mod.server.modules.attack.command.ConfigureTargetCommand;
import pw.tales.cofdsystem.mod.server.modules.attack.views.NoGameObjectView;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.GOEntityRelation;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.exceptions.EntityNotBoundException;
import pw.tales.cofdsystem.mod.server.modules.go_source.events.GameObjectUnloadingEvent;

@Singleton
public class AttackModule extends ServerCommandModule {

  public final AttackManager attackManager;
  private final GOEntityRelation entityRelation;

  @Inject
  public AttackModule(
      GOEntityRelation entityRelation,
      AttackManager attackManager,
      Injector injector
  ) {
    super(injector);
    this.attackManager = attackManager;
    this.entityRelation = entityRelation;
  }

  @Override
  public void setUp() {
    EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void onGameObjectUnloading(GameObjectUnloadingEvent.PRE event) {
    this.attackManager.clearGOAttacks(event.getGameObject());
  }

  public CompletableFuture<Attack> attack(Entity mcAttacker, Entity mcTarget) {
    TalesSystem.logger.info(
        "{} initiated attack against {}", mcAttacker, mcTarget
    );

    return Utils.merge(
        this.entityRelation.getGameObject(mcAttacker, false),
        this.entityRelation.getGameObject(mcTarget)
    ).thenApply(pair -> {
      GameObject attacker = pair.getLeft();
      GameObject target = pair.getRight();

      TalesSystem.logger.info(
          "{} as {} initiated attack against {} as {}",
          mcAttacker.getName(), attacker,
          mcTarget.getName(), target
      );

      return this.attackManager.create(attacker, target);
    }).exceptionally(origException -> {

      if (origException instanceof EntityNotBoundException) {
        this.handleNoGameObject(
            mcAttacker,
            (EntityNotBoundException) origException
        );
      } else {
        origException.printStackTrace();
      }

      return null;
    });
  }

  private void handleNoGameObject(Entity recipient, EntityNotBoundException e) {
    if (!(recipient instanceof EntityPlayerMP)) {
      return;
    }

    EntityPlayerMP player = (EntityPlayerMP) recipient;
    NoGameObjectView view = new NoGameObjectView(e.getHolder());
    recipient.sendMessage(view.build(player));
  }

  @Override
  public Set<Class<? extends CommandBase>> getCommandClasses() {
    return ImmutableSet.of(
        AttackShowCommand.class,
        ConfigureActorCommand.class,
        ConfigureTargetCommand.class
    );
  }
}
