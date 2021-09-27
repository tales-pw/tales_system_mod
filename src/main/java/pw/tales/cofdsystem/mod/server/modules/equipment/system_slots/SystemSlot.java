package pw.tales.cofdsystem.mod.server.modules.equipment.system_slots;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import pw.tales.cofdsystem.mod.server.modules.equipment.EquipmentModule;

public abstract class SystemSlot {

  protected final EquipmentModule module;

  protected SystemSlot(
      EquipmentModule module
  ) {
    this.module = module;
  }

  public void update(EntityLivingBase entity, EntityEquipmentSlot slot, ItemStack itemStack) {
    if (this.getSlot() != slot) {
      return;
    }

    this.updateWithAction(entity, itemStack);
  }

  protected abstract EntityEquipmentSlot getSlot();

  protected abstract void updateWithAction(EntityLivingBase entity, ItemStack itemStack);

  public void init(EntityLivingBase entity) {
    EntityEquipmentSlot slot = this.getSlot();
    this.updateWithoutAction(
        entity,
        entity.getItemStackFromSlot(slot)
    );
  }

  protected abstract void updateWithoutAction(EntityLivingBase entity, ItemStack itemStack);
}
