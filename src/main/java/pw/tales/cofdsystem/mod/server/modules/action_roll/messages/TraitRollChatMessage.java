package pw.tales.cofdsystem.mod.server.modules.action_roll.messages;

import java.util.List;
import java.util.Map;
import pw.tales.cofdsystem.action.events.roll.ActionPostRollEvent;
import pw.tales.cofdsystem.action.opposition.pool.ActionPool;
import pw.tales.cofdsystem.dices.RollResponse;
import pw.tales.cofdsystem.dices.requests.RollRequestTrait;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.common.haxe_adapters.HaxeArrayAdapter;
import pw.tales.cofdsystem.mod.common.haxe_adapters.HaxeMapAdapter;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.renderer.ITemplate;
import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.MessageState;
import ru.xunto.roleplaychat.framework.state.Property;

public class TraitRollChatMessage extends RollChatMessage<RollRequestTrait> {

  public static final IProperty<Map<String, Object>> modifiers = new Property<>("modifiers");
  public static final IProperty<List<String>> traits = new Property<>("traits");
  public static final IProperty<Integer> modifier = new Property<>("modifier");

  private static final ITemplate template =
      new PebbleDetailsTemplate("assets/tales_system/templates/roll_message.twig",
          "assets/tales_system/templates/trait_roll_details.twig");

  public TraitRollChatMessage(ActionPostRollEvent event) {
    this(event.getRoll());
  }

  public TraitRollChatMessage(ActionPool roll) {
    this(
        roll.getGameObject(),
        roll.getRequest(),
        roll.getResponse()
    );
  }

  public TraitRollChatMessage(
      GameObject gameObject,
      RollRequestTrait request,
      RollResponse response
  ) {
    super(gameObject, request, response);
  }

  @Override
  public Environment create() {
    Environment environment = super.create();
    MessageState state = environment.getState();

    state.setValue(modifiers, new HaxeMapAdapter<>(request.getAppliedModifiers()));
    state.setValue(traits, new HaxeArrayAdapter<>(request.getTraits()));
    state.setValue(modifier, request.processModifiers());

    environment.setTemplate(template);
    return environment;
  }
}
