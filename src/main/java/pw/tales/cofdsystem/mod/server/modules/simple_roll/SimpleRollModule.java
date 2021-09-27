package pw.tales.cofdsystem.mod.server.modules.simple_roll;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Injector;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.dices.IRollRequest;
import pw.tales.cofdsystem.dices.RollResponse;
import pw.tales.cofdsystem.mod.server.modules.ServerCommandModule;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.GOEntityRelation;
import pw.tales.cofdsystem.mod.server.modules.simple_roll.command.SimpleRollCommand;
import pw.tales.cofdsystem.mod.server.modules.simple_roll.events.SimpleRollEvent.RollResult;
import pw.tales.cofdsystem.mod.server.modules.simple_roll.rpchat_messages.SimpleRollMessage;
import ru.xunto.roleplaychat.RoleplayChatCore;
import ru.xunto.roleplaychat.forge_1_12_2.ForgeSpeaker;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.api.Request;

@Singleton
public class SimpleRollModule extends ServerCommandModule {

  private final CofDSystem system;
  private final GOEntityRelation goEntityRelation;

  @Inject
  public SimpleRollModule(
      CofDSystem system,
      GOEntityRelation goEntityRelation,
      Injector injector
  ) {
    super(injector);
    this.system = system;
    this.goEntityRelation = goEntityRelation;
  }

  public void setUp() {
    EVENT_BUS.register(this);
  }

  @Override
  public Set<Class<? extends CommandBase>> getCommandClasses() {
    return ImmutableSet.of(SimpleRollCommand.class);
  }

  public void roll(EntityPlayerMP player, IRollRequest request) {
    RollResponse response = system.dices.roll(request);
    EVENT_BUS.post(new RollResult(player, request, response));
  }

  @SubscribeEvent
  public void onSimpleRollEvent(RollResult event) {
    EntityPlayerMP player = event.getSender();

    this.goEntityRelation.getGameObject(player).thenApply((gameObject -> {
      Environment environment = new SimpleRollMessage(
          gameObject,
          event.getRequest(),
          event.getResponse()
      ).create();

      RoleplayChatCore.instance.process(
          new Request(
              "",
              new ForgeSpeaker(player)
          ),
          environment
      );

      return gameObject;
    }));
  }
}
