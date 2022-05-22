package pw.tales.cofdsystem.mod.server.modules.equipment.views;

import java.util.List;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import pw.tales.cofdsystem.game_object.traits.TraitType;
import pw.tales.cofdsystem.mod.common.haxe_adapters.HaxeArrayAdapter;
import pw.tales.cofdsystem.mod.server.views.TextComponentEmpty;
import pw.tales.cofdsystem.weapon.Weapon;

public class WeaponStatsView extends StatsView {

  private static final int TAG_LINE_BREAK = 2;

  private final Weapon weapon;

  public WeaponStatsView(Weapon weapon) {
    this.weapon = weapon;
  }

  @Override
  public ITextComponent build(ServerPlayerEntity viewer) {
    ITextComponent header = this.buildHeader("Предмет является оружием:");

    List<TraitType> tags = new HaxeArrayAdapter<>(this.weapon.getWeaponTags());

    return new TextComponentEmpty()
        .appendSibling(header)
        .appendSibling(this.buildKV("Урон", this.weapon.getDamageMod()))
        .appendText("\n")
        .appendSibling(this.buildKV("Инициатива", this.weapon.getInitiativeMod()))
        .appendText("\n")
        .appendSibling(this.buildTags("Тэги", tags));
  }

  public ITextComponent buildTags(String label, List<TraitType> weaponTags) {
    if (weaponTags.isEmpty()) {
      return new TextComponentEmpty();
    }

    ITextComponent header = new TextComponentString(label).appendText(":\n");
    header.getStyle().setColor(TextFormatting.YELLOW);

    ITextComponent tagsComponent = new TextComponentString("");
    tagsComponent.getStyle().setColor(TextFormatting.WHITE);

    for (int i = 0; i < weaponTags.size(); i++) {
      TraitType weaponTag = weaponTags.get(i);
      String displayName = weaponTag.getDisplayName();

      tagsComponent.appendText(displayName);

      if (i < weaponTags.size() - 1) {
        tagsComponent.appendText(",");

        if (i % TAG_LINE_BREAK == 1) {
          tagsComponent.appendText("\n");
        } else {
          tagsComponent.appendText(" ");
        }
      }
    }

    return new TextComponentEmpty()
        .appendSibling(header)
        .appendSibling(tagsComponent);
  }
}
