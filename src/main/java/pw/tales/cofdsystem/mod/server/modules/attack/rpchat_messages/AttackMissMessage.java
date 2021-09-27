package pw.tales.cofdsystem.mod.server.modules.attack.rpchat_messages;

import java.util.Map;
import pw.tales.cofdsystem.character.Character;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.server.roleplaychat.IMessage;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.pebble.PebbleChatTemplate;
import ru.xunto.roleplaychat.framework.renderer.ITemplate;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;

public class AttackMissMessage implements IMessage {

  private static final ITemplate template = new PebbleChatTemplate(
      "assets/tales_system/templates/miss_message.twig");

  private final GameObject gameObject;

  public AttackMissMessage(GameObject gameObject) {
    this.gameObject = gameObject;
  }

  public Environment create() {
    String actorName = new Character(this.gameObject).getName();

    Environment environment = new Environment(actorName, "");
    environment.setProcessed(true);

    // Setup colors
    Map<String, TextColor> colors = environment.getColors();
    colors.put("default", TextColor.GRAY);

    // Set template
    environment.setTemplate(template);
    return environment;
  }
}
