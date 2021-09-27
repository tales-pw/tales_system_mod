package pw.tales.cofdsystem.mod.server.modules.equipment.rpchat_messages;

import java.util.Map;
import pw.tales.cofdsystem.armor.Armor;
import pw.tales.cofdsystem.armor.IArmor;
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

public class ArmorMessage implements IMessage {

  private static final IProperty<IArmor> ARMOR_PROPERTY = new Property<>("armor");
  private static final ITemplate ITEM_TEMPLATE = new PebbleChatTemplate(
      "assets/tales_system/templates/armor_message.twig");

  private final GameObject actor;
  private final Armor armor;

  public ArmorMessage(GameObject actor, Armor armor) {
    this.actor = actor;
    this.armor = armor;
  }

  public Environment create() {
    String actorName = new Character(actor).getName();

    Environment environment = new Environment(actorName, "");
    environment.setProcessed(true);

    // Set up state
    MessageState state = environment.getState();
    state.setValue(ARMOR_PROPERTY, armor);

    // Setup colors
    Map<String, TextColor> colors = environment.getColors();
    colors.put("default", TextColor.GRAY);

    // Set template
    environment.setTemplate(ITEM_TEMPLATE);

    return environment;
  }
}
