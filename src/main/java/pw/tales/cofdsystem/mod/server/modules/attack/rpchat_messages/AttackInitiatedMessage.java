package pw.tales.cofdsystem.mod.server.modules.attack.rpchat_messages;

import java.util.Map;
import pw.tales.cofdsystem.character.Character;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.server.roleplaychat.IMessage;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.pebble.PebbleChatTemplate;
import ru.xunto.roleplaychat.framework.renderer.ITemplate;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;
import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.MessageState;
import ru.xunto.roleplaychat.framework.state.Property;

public class AttackInitiatedMessage implements IMessage {

  public static final IProperty<String> TARGET_PROPERTY = new Property<>("target");
  private static final ITemplate template = new PebbleChatTemplate(
      "assets/tales_system/templates/attack_message.twig"
  );

  private final GameObject actor;
  private final GameObject target;

  public AttackInitiatedMessage(GameObject actor, GameObject target) {
    this.actor = actor;
    this.target = target;
  }

  public Environment create() {
    String actorName = new Character(this.actor).getName();
    String targetName = new Character(this.target).getName();

    Environment environment = new Environment(actorName, "");
    environment.setProcessed(true);

    MessageState state = environment.getState();
    state.setValue(TARGET_PROPERTY, targetName);

    // Setup colors
    Map<String, TextColor> colors = environment.getColors();
    colors.put("target", TextColor.GREEN);
    colors.put("default", TextColor.GRAY);

    // Set template
    environment.setTemplate(template);
    return environment;
  }
}
