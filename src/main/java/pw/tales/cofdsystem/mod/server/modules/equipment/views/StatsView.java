package pw.tales.cofdsystem.mod.server.modules.equipment.views;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import pw.tales.cofdsystem.mod.server.views.View;

public abstract class StatsView extends View {

  protected ITextComponent buildStat(String label, Integer value) {
    return this.buildStat(label, Integer.toString(value));
  }

  protected ITextComponent buildStat(String label, String value) {
    TextComponentString statName = new TextComponentString(label);
    statName.getStyle().setColor(TextFormatting.YELLOW);

    TextComponentString statValue = new TextComponentString(value);
    statValue.getStyle().setColor(TextFormatting.GRAY);

    return statName.appendText(":").appendSibling(statValue).appendText("\n");
  }
}
