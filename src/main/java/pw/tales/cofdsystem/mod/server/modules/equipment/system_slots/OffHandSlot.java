package pw.tales.cofdsystem.mod.server.modules.equipment.system_slots;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import pw.tales.cofdsystem.common.EnumHand;
import pw.tales.cofdsystem.mod.server.modules.equipment.EquipmentModule;

public class OffHandSlot extends SystemSlot {

  public OffHandSlot(EquipmentModule module) {
    super(module);
  }

  @Override
  protected EntityEquipmentSlot getSlot() {
    return EntityEquipmentSlot.OFFHAND;
  }

  @Override
  protected void updateWithAction(EntityLivingBase entity, ItemStack itemStack) {
    this.module.performActionPickItem(entity, itemStack, EnumHand.OFFHAND);
  }

  @Override
  protected void updateWithoutAction(EntityLivingBase entity, ItemStack itemStack) {
    this.module.updateWeaponTrait(entity, itemStack, EnumHand.OFFHAND);
  }
}
