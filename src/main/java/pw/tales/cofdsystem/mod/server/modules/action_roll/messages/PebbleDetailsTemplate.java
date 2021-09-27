package pw.tales.cofdsystem.mod.server.modules.action_roll.messages;

import java.util.Map;
import ru.xunto.roleplaychat.framework.pebble.PebbleChatTemplate;
import ru.xunto.roleplaychat.framework.renderer.text.Text;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;
import ru.xunto.roleplaychat.framework.renderer.text.TextComponent;
import ru.xunto.roleplaychat.framework.state.MessageState;

public class PebbleDetailsTemplate extends PebbleChatTemplate {

  private final PebbleChatTemplate detailsTemplate;

  public PebbleDetailsTemplate(String pathMain, String pathLabel) {
    super(pathMain);
    this.detailsTemplate = new PebbleChatTemplate(pathLabel);
  }

  @Override
  public Text render(MessageState state, Map<String, TextColor> colors) {
    Text message = super.render(state, colors);
    colors.put("default", TextColor.WHITE);

    Text details = detailsTemplate.render(state, colors);

    TextComponent hoverComponent = new TextComponent(" [?] ", TextColor.WHITE);
    hoverComponent.setHoverText(details);

    message.add(hoverComponent);

    return message;
  }
}
