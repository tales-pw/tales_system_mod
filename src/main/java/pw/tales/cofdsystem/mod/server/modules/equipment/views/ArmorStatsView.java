package pw.tales.cofdsystem.mod.server.modules.equipment.views;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import pw.tales.cofdsystem.armor.Armor;
import pw.tales.cofdsystem.mod.server.views.TextComponentEmpty;

public class ArmorStatsView extends StatsView {

  private final Armor armor;

  public ArmorStatsView(Armor armor) {
    this.armor = armor;
  }

  @Override
  public ITextComponent build(EntityPlayerMP viewer) {
    ITextComponent header = this.buildHeader("Предмет является доспехом:");
    return new TextComponentEmpty()
        .appendSibling(header)
        .appendSibling(this.buildStat("Сила", this.armor.getStrengthReq()))
        .appendSibling(this.buildStat("Защита", this.armor.getDefenceMod()))
        .appendSibling(this.buildStat("Скорость", this.armor.getSpeedMod()))
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
    return this.buildStat(label, rating);
  }
}
