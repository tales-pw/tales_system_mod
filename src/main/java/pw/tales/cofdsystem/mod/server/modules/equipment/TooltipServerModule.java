package pw.tales.cofdsystem.mod.server.modules.equipment;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import pw.tales.cofdsystem.mod.Utils;
import pw.tales.cofdsystem.mod.common.IModule;
import pw.tales.cofdsystem.mod.server.modules.equipment.views.ArmorStatsView;
import pw.tales.cofdsystem.mod.server.modules.equipment.views.WeaponStatsView;
import pw.tales.cofdsystem.mod.server.views.TextComponentEmpty;

@Singleton
public class TooltipServerModule implements IModule {

  private final EquipmentModule equipmentModule;

  @Inject
  public TooltipServerModule(EquipmentModule equipmentModule) {
    this.equipmentModule = equipmentModule;
  }

  public CompletableFuture<ITextComponent> buildTooltip(
      EntityPlayerMP player,
      ItemStack itemStack
  ) {
    return Utils.merge(
        this.buildArmorTooltip(player, itemStack),
        this.buildWeaponTooltip(player, itemStack)
    ).thenApply(pair -> {
      ITextComponent armorStats = pair.getLeft();
      ITextComponent weaponStats = pair.getRight();

      TextComponentEmpty component = new TextComponentEmpty();

      if (weaponStats != null) {
        component.appendSibling(weaponStats);
      }

      if (weaponStats != null && armorStats != null) {
        component.appendText("\n");
      }

      if (armorStats != null) {
        component.appendSibling(armorStats);
      }

      return component;
    });
  }

  public CompletableFuture<ITextComponent> buildArmorTooltip(
      EntityPlayerMP player,
      ItemStack itemStack
  ) {
    return this.equipmentModule.getArmor(itemStack).thenApply(armor -> {
      if (armor == null) {
        return null;
      }

      return new ArmorStatsView(armor).build(player);
    });
  }

  public CompletableFuture<ITextComponent> buildWeaponTooltip(
      EntityPlayerMP player,
      ItemStack itemStack
  ) {

    return this.equipmentModule.getWeapon(itemStack).thenApply(weapon -> {
      if (weapon == null) {
        return null;
      }
      return new WeaponStatsView(weapon).build(player);
    });
  }
}
