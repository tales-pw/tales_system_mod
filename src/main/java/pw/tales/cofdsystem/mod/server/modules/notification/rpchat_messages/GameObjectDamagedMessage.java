package pw.tales.cofdsystem.mod.server.modules.notification.rpchat_messages;

import java.util.Map;
import pw.tales.cofdsystem.character.Character;
import pw.tales.cofdsystem.character.traits.advantages.health.events.GameObjectDamagedEvent;
import pw.tales.cofdsystem.damage.Damage;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.server.roleplaychat.IMessage;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.pebble.PebbleChatTemplate;
import ru.xunto.roleplaychat.framework.renderer.ITemplate;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;
import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.MessageState;
import ru.xunto.roleplaychat.framework.state.Property;

/**
 * Message about GameObject being damaged.
 */
public class GameObjectDamagedMessage implements IMessage {

  public static final ITemplate template = new PebbleChatTemplate(
      "assets/tales_system/templates/game_object_damaged.twig");

  public static final IProperty<Integer> BASHING = new Property<>("bashing");
  public static final IProperty<Integer> LETHAL = new Property<>("lethal");
  public static final IProperty<Integer> AGGRAVATED = new Property<>("aggravated");

  private final GameObject gameObject;
  private final Damage damage;

  public GameObjectDamagedMessage(
      GameObjectDamagedEvent event
  ) {
    this.gameObject = event.getGameObject();
    this.damage = event.getDamage();
  }

  public Environment create() {
    String actorName = new Character(this.gameObject).getName();

    Environment environment = new Environment(actorName, "");
    MessageState state = environment.getState();

    environment.setProcessed(true);

    // Set field values
    state.setValue(GameObjectDamagedMessage.BASHING, damage.getBashing());
    state.setValue(GameObjectDamagedMessage.LETHAL, damage.getLethal());
    state.setValue(GameObjectDamagedMessage.AGGRAVATED, damage.getAggravated());

    // Setup colors
    Map<String, TextColor> colors = environment.getColors();
    colors.put("default", TextColor.GRAY);

    // Set template
    environment.setTemplate(template);
    return environment;
  }
}
