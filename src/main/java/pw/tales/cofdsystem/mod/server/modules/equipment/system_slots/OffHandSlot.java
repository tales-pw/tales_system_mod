package pw.tales.cofdsystem.mod.server.modules.equipment.system_slots;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import pw.tales.cofdsystem.common.EnumHand;
import pw.tales.cofdsystem.mod.server.modules.equipment.EquipmentModule;

@Singleton
public class OffHandSlot extends SystemSlot {

  @Inject
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
