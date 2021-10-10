package pw.tales.cofdsystem.mod.server.views;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

public class ChatActionsBuilder {

  private final List<ITextComponent> actions = new ArrayList<>();
  private final TextFormatting color;
  private final int perLine;

  public ChatActionsBuilder(TextFormatting color) {
    this(color, 3);
  }

  public ChatActionsBuilder(TextFormatting color, int perLine) {
    this.color = color;
    this.perLine = perLine;
  }

  public ChatActionsBuilder addText(String text, String command) {
    return this.addText(text, command, false);
  }

  public ChatActionsBuilder addText(String text, String command, boolean active) {
    ITextComponent component = new TextComponentTranslation(text);
    return this.add(component, command, active);
  }

  public ChatActionsBuilder add(ITextComponent component, String command, boolean active) {
    if (active) {
      Style style = component.getStyle();
      style.setUnderlined(true);
      style.setColor(TextFormatting.GREEN);
    }

    return this.add(component, command);
  }

  public ChatActionsBuilder add(ITextComponent component, String command) {
    component = new TextComponentString("[").appendSibling(component).appendText("] ");
    component.getStyle().setClickEvent(new ClickEvent(
        ClickEvent.Action.RUN_COMMAND, command
    )).setColor(this.color);
    actions.add(component);

    return this;
  }

  public ITextComponent build() {
    TextComponentString result = new TextComponentEmpty();

    int i = 0;
    for (ITextComponent actionComponent : actions) {
      if (i >= this.perLine) {
        i = 0;
        result.appendText("\n");
      }
      result.appendSibling(actionComponent);
      i++;
    }

    return result;
  }
}
