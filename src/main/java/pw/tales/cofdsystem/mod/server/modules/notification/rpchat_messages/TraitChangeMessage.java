package pw.tales.cofdsystem.mod.server.modules.notification.rpchat_messages;

import java.util.Map;
import pw.tales.cofdsystem.character.Character;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.game_object.events.traits.TraitPostEvent;
import pw.tales.cofdsystem.game_object.traits.Trait;
import pw.tales.cofdsystem.mod.server.roleplaychat.IMessage;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.renderer.ITemplate;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;
import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.Property;

/**
 * Base class for any trait change message.
 */
public abstract class TraitChangeMessage implements IMessage {

  private static final IProperty<String> traitName = new Property<>("trait_name");

  private final GameObject gameObject;
  private final Trait newTrait;
  private final ITemplate template;


  protected TraitChangeMessage(TraitPostEvent event, ITemplate template) {
    this(
        event.getGameObject(),
        event.getTrait(),
        template
    );
  }

  protected TraitChangeMessage(GameObject gameObject, Trait newTrait, ITemplate template) {
    this.gameObject = gameObject;
    this.newTrait = newTrait;
    this.template = template;
  }

  public Environment create() {
    String actorName = new Character(this.gameObject).getName();
    Environment environment = new Environment(actorName, "");

    environment.getState().setValue(traitName, newTrait.getDisplayName());

    // Setup colors
    Map<String, TextColor> colors = environment.getColors();
    colors.put("default", TextColor.GRAY);
    colors.put("trait_name", TextColor.WHITE);

    // Set template
    environment.setTemplate(template);
    return environment;
  }
}
