package pw.tales.cofdsystem.mod.server.modules.equipment.system_slots;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import pw.tales.cofdsystem.mod.server.modules.equipment.EquipmentModule;

@Singleton
public class ArmorSlot extends SystemSlot {

  @Inject
  public ArmorSlot(EquipmentModule module) {
    super(module);
  }

  @Override
  protected EntityEquipmentSlot getSlot() {
    return EntityEquipmentSlot.CHEST;
  }

  @Override
  protected void updateWithAction(EntityLivingBase entity, ItemStack itemStack) {
    this.module.performActionDonArmor(entity, itemStack);
  }

  @Override
  protected void updateWithoutAction(EntityLivingBase entity, ItemStack itemStack) {
    this.module.updateArmorTrait(entity, itemStack);
  }
}
