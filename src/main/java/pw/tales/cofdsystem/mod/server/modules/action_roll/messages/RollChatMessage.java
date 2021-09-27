package pw.tales.cofdsystem.mod.server.modules.action_roll.messages;

import java.util.List;
import java.util.Map;
import pw.tales.cofdsystem.character.Character;
import pw.tales.cofdsystem.dices.EnumExplode;
import pw.tales.cofdsystem.dices.EnumResult;
import pw.tales.cofdsystem.dices.IRollRequest;
import pw.tales.cofdsystem.dices.RollResponse;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.common.haxe_adapters.HaxeArrayAdapter;
import pw.tales.cofdsystem.mod.server.roleplaychat.IMessage;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;
import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.MessageState;
import ru.xunto.roleplaychat.framework.state.Property;

public abstract class RollChatMessage<T extends IRollRequest> implements IMessage {

  public static final IProperty<EnumExplode> permutation = new Property<>("permutation");
  public static final IProperty<Integer> poolSize = new Property<>("pool_size");
  public static final IProperty<Integer> successes = new Property<>("successes");
  public static final IProperty<List<Object>> diceResults = new Property<>("dice_results");
  public static final IProperty<EnumResult> result = new Property<>("result");

  protected final GameObject gameObject;
  protected final T request;
  protected final RollResponse response;

  protected RollChatMessage(
      GameObject gameObject,
      T request,
      RollResponse response
  ) {

    this.gameObject = gameObject;
    this.request = request;
    this.response = response;
  }

  public Environment create() {
    String name = new Character(gameObject).getName();

    Environment environment = new Environment(name, "");
    MessageState state = environment.getState();

    environment.setProcessed(true);

    // Set field values
    state.setValue(permutation, request.getExplode());
    state.setValue(result, response.getResult());
    state.setValue(poolSize, response.getPoolSize());
    state.setValue(successes, response.getSuccesses());
    state.setValue(diceResults, new HaxeArrayAdapter<>(response.getResults()));

    // Setup colors
    Map<String, TextColor> colors = environment.getColors();
    colors.put("dice_dramatic_failure", TextColor.DARK_RED);
    colors.put("dice_failure", TextColor.RED);
    colors.put("dice_explode", TextColor.DARK_GREEN);
    colors.put("dice_success", TextColor.GREEN);
    colors.put("dice_reroll", TextColor.DARK_PURPLE);
    colors.put("default", TextColor.GRAY);

    return environment;
  }
}
