package pw.tales.cofdsystem.mod.server.modules.equipment.views;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import pw.tales.cofdsystem.armor.Armor;
import pw.tales.cofdsystem.mod.server.views.TextComponentEmpty;

public class ArmorStatsView extends StatsView {

  private final Armor armor;

  public ArmorStatsView(Armor armor) {
    this.armor = armor;
  }

  @Override
  public ITextComponent build(ServerPlayerEntity viewer) {
    ITextComponent header = this.buildHeader("Предмет является доспехом:");
    return new TextComponentEmpty()
        .appendSibling(header)
        .appendSibling(this.buildKV("Сила", this.armor.getStrengthReq()))
        .appendText("\n")
        .appendSibling(this.buildKV("Защита", this.armor.getDefenceMod()))
        .appendText("\n")
        .appendSibling(this.buildKV("Скорость", this.armor.getSpeedMod()))
        .appendText("\n")
        .appendSibling(this.buildRating(
            "Рейтинг",
            this.armor.getGeneral(),
            this.armor.getBallistic()
        ));
  }

  private ITextComponent buildRating(String label, int general, int ballistic) {
    String rating = String.format(
        "%d/%d",
        general,
        ballistic
    );
    return this.buildKV(label, rating);
  }
}
