package pw.tales.cofdsystem.mod.server.modules.notification.rpchat_messages;

import pw.tales.cofdsystem.game_object.events.traits.TraitPostAttachEvent;
import ru.xunto.roleplaychat.framework.pebble.PebbleChatTemplate;
import ru.xunto.roleplaychat.framework.renderer.ITemplate;

/**
 * Message about trait being added to GameObject.
 */
public class TraitAddedMessage extends TraitChangeMessage {

  private static final ITemplate template = new PebbleChatTemplate(
      "assets/tales_system/templates/game_object_trait_add.twig");

  public TraitAddedMessage(
      TraitPostAttachEvent event
  ) {
    super(event, template);
  }
}
