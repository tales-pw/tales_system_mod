package pw.tales.cofdsystem.mod.server.modules.notification.rpchat_messages;

import java.util.Map;
import pw.tales.cofdsystem.character.Character;
import pw.tales.cofdsystem.character.traits.advantages.health.events.GameObjectDiedEvent;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.server.roleplaychat.IMessage;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.pebble.PebbleChatTemplate;
import ru.xunto.roleplaychat.framework.renderer.ITemplate;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;

/**
 * Message about GameObject dying.
 */
public class GameObjectDiedMessage implements IMessage {

  private static final ITemplate template = new PebbleChatTemplate(
      "assets/tales_system/templates/game_object_died.twig");
  private final GameObject gameObject;

  public GameObjectDiedMessage(
      GameObjectDiedEvent event
  ) {
    this.gameObject = event.getGameObject();
  }

  public Environment create() {
    String actorName = new Character(this.gameObject).getName();

    Environment environment = new Environment(actorName, "");

    // Setup colors
    Map<String, TextColor> colors = environment.getColors();
    colors.put("default", TextColor.GRAY);

    // Set template
    environment.setTemplate(template);
    return environment;
  }
}
