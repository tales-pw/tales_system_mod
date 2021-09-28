package pw.tales.cofdsystem.mod.server.modules.position.view;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import pw.tales.cofdsystem.common.EnumRange;
import pw.tales.cofdsystem.mod.server.views.TextComponentEmpty;
import pw.tales.cofdsystem.mod.server.views.View;

public class RangeView extends View {

  public static final DecimalFormat DISTANCE_FORMAT = new DecimalFormat(
      "#.#",
      DecimalFormatSymbols.getInstance(Locale.ENGLISH)
  );

  private final double distance;
  private final EnumRange range;

  public RangeView(double distance, EnumRange range) {
    this.distance = distance;
    this.range = range;
  }

  @Override
  public ITextComponent build(EntityPlayerMP viewer) {
    return new TextComponentEmpty()
        .appendText("\n")
        .appendSibling(this.buildHeader("Дальность"))
        .appendSibling(this.buildRange())
        .appendSibling(this.buildDistance());
  }

  private ITextComponent buildRange() {
    ITextComponent label = new TextComponentString(
        "Система:"
    );
    label.getStyle().setColor(TextFormatting.YELLOW);

    ITextComponent value = new TextComponentTranslation(
        getRangeTranslationKey(this.range)
    );
    value.getStyle().setColor(TextFormatting.GRAY);

    return label.appendText(" ").appendSibling(value).appendText("\n");
  }

  public static String getRangeTranslationKey(EnumRange range) {
    return String.format("cofd.range.%s", range.name);
  }

  private ITextComponent buildDistance() {
    ITextComponent label = new TextComponentString(
        "Число:"
    );
    label.getStyle().setColor(TextFormatting.YELLOW);

    ITextComponent value = new TextComponentString(
        DISTANCE_FORMAT.format(this.distance)
    );
    value.getStyle().setColor(TextFormatting.GRAY);

    return label.appendText(" ").appendSibling(value);
  }
}
