package pw.tales.cofdsystem.mod.server.modules.equipment.rpchat_messages;

import java.util.Map;
import pw.tales.cofdsystem.character.Character;
import pw.tales.cofdsystem.common.EnumHand;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.server.roleplaychat.IMessage;
import pw.tales.cofdsystem.weapon.Weapon;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.pebble.PebbleChatTemplate;
import ru.xunto.roleplaychat.framework.renderer.ITemplate;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;
import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.MessageState;
import ru.xunto.roleplaychat.framework.state.Property;

public class HandItemMessage implements IMessage {

  public static final IProperty<Weapon> WEAPON_PROPERTY = new Property<>("weapon");
  public static final IProperty<EnumHand> HAND_PROPERTY = new Property<>("hand");
  public static final ITemplate TEMPLATE = new PebbleChatTemplate(
      "assets/tales_system/templates/item_message.twig"
  );

  private final GameObject actor;
  private final Weapon weapon;
  private final EnumHand hand;

  public HandItemMessage(GameObject actor, Weapon weapon, EnumHand hand) {
    this.actor = actor;
    this.weapon = weapon;
    this.hand = hand;
  }

  public Environment create() {
    String actorName = new Character(this.actor).getName();

    Environment environment = new Environment(actorName, "");
    environment.setProcessed(true);

    // Set up state
    MessageState state = environment.getState();
    state.setValue(WEAPON_PROPERTY, this.weapon);
    state.setValue(HAND_PROPERTY, this.hand);

    // Setup colors
    Map<String, TextColor> colors = environment.getColors();
    colors.put("default", TextColor.GRAY);

    // Set template
    environment.setTemplate(TEMPLATE);

    return environment;
  }
}
