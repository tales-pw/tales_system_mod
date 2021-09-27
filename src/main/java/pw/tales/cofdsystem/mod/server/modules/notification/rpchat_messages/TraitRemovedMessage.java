package pw.tales.cofdsystem.mod.server.modules.notification.rpchat_messages;

import pw.tales.cofdsystem.game_object.events.traits.TraitPostRemoveEvent;
import ru.xunto.roleplaychat.framework.pebble.PebbleChatTemplate;
import ru.xunto.roleplaychat.framework.renderer.ITemplate;

/**
 *
 */
public class TraitRemovedMessage extends TraitChangeMessage {

  private static final ITemplate template = new PebbleChatTemplate(
      "assets/tales_system/templates/game_object_trait_removed.twig");

  public TraitRemovedMessage(
      TraitPostRemoveEvent event
  ) {
    super(event, template);
  }
}