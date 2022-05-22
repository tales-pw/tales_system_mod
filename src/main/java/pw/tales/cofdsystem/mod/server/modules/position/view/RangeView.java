package pw.tales.cofdsystem.mod.server.modules.position.view;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import pw.tales.cofdsystem.common.EnumRange;
import pw.tales.cofdsystem.mod.server.views.TextComponentEmpty;
import pw.tales.cofdsystem.mod.server.views.View;

/**
 * View for range command output.
 */
public class RangeView extends View {

  public static final DecimalFormat DISTANCE_FORMAT = new DecimalFormat(
      "#.#",
      DecimalFormatSymbols.getInstance(Locale.ENGLISH)
  );

  private final ICommandSender from;
  private final ICommandSender to;
  private final double distance;
  private final EnumRange range;

  public RangeView(
      ICommandSender from,
      ICommandSender to,
      double distance,
      EnumRange range
  ) {
    this.from = from;
    this.to = to;
    this.distance = distance;
    this.range = range;
  }

  @Override
  public ITextComponent build(ServerPlayerEntity viewer) {
    return new TextComponentEmpty()
        .appendSibling(this.buildHeader("Расстояние"))
        .appendSibling(this.buildKV("от", this.from))
        .appendText("\n")
        .appendSibling(this.buildKV("до", this.to))
        .appendText("\n")
        .appendSibling(this.buildKVDistance("Система", this.range))
        .appendText("\n")
        .appendSibling(this.buildKVDistance("Число", this.distance));
  }

  /**
   * Build component with label and value.
   *
   * @param key       Label text.
   * @param enumRange EnumRange object.
   * @return component
   */
  private ITextComponent buildKVDistance(String key, EnumRange enumRange) {
    return this.buildKV(key, getRangeTranslationKey(enumRange));
  }

  /**
   * Get translation key for range.
   *
   * @param range EnumRange object.
   * @return translation key
   */
  private static String getRangeTranslationKey(EnumRange range) {
    return String.format("cofd.range.%s", range.name);
  }

  /**
   * Build component with label and value.
   *
   * @param key      Label text.
   * @param distance Distance.
   * @return component
   */
  private ITextComponent buildKVDistance(String key, double distance) {
    String distanceText = DISTANCE_FORMAT.format(distance);
    return this.buildKV(key, String.format("%sm", distanceText));
  }
}
