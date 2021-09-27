package pw.tales.cofdsystem.mod.server.modules.simple_roll.rpchat_messages;

import pw.tales.cofdsystem.dices.IRollRequest;
import pw.tales.cofdsystem.dices.RollResponse;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.server.modules.action_roll.messages.PebbleDetailsTemplate;
import pw.tales.cofdsystem.mod.server.modules.action_roll.messages.RollChatMessage;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.renderer.ITemplate;

public class SimpleRollMessage extends RollChatMessage<IRollRequest> {

  private static final ITemplate template =
      new PebbleDetailsTemplate("assets/tales_system/templates/roll_message.twig",
          "assets/tales_system/templates/roll_details.twig");

  public SimpleRollMessage(GameObject gameObject, IRollRequest request, RollResponse response) {
    super(gameObject, request, response);
  }

  @Override
  public Environment create() {
    Environment environment = super.create();
    environment.setTemplate(template);
    return environment;
  }
}
