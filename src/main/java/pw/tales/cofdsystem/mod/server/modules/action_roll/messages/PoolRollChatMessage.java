package pw.tales.cofdsystem.mod.server.modules.action_roll.messages;

import pw.tales.cofdsystem.dices.RollResponse;
import pw.tales.cofdsystem.dices.requests.RollRequestPool;
import pw.tales.cofdsystem.game_object.GameObject;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.renderer.ITemplate;

public class PoolRollChatMessage extends RollChatMessage<RollRequestPool> {

  private static final ITemplate template =
      new PebbleDetailsTemplate(
          "assets/tales_system/templates/roll_message.twig",
          "assets/tales_system/templates/roll_details.twig"
      );

  public PoolRollChatMessage(
      GameObject gameObject,
      RollRequestPool request,
      RollResponse response
  ) {
    super(gameObject, request, response);
  }

  @Override
  public Environment create() {
    Environment environment = super.create();
    environment.setTemplate(template);
    return environment;
  }
}
