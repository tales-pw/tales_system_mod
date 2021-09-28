package pw.tales.cofdsystem.mod.server.modules.equipment;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.mockito.Mockito;
import pw.tales.cofdsystem.common.EnumHand;
import pw.tales.cofdsystem.mod.TalesSystemTest;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.GOEntityRelation;
import pw.tales.cofdsystem.mod.server.modules.go_relation_item.GOItemRelation;

public class SystemSlotsModuleTest extends TalesSystemTest {

  private EquipmentModule equipmentModule;
  private SystemSlotsModule slotsHandler;

  public void setUp() throws Exception {
    this.equipmentModule = mock(EquipmentModule.class, Mockito.RETURNS_DEEP_STUBS);
    GOItemRelation goItemRelation = mock(GOItemRelation.class, Mockito.RETURNS_DEEP_STUBS);
    GOEntityRelation goEntityRelation = mock(GOEntityRelation.class, Mockito.RETURNS_DEEP_STUBS);
    super.setUp();
    this.slotsHandler = new SystemSlotsModule(
        this.equipmentModule,
        goEntityRelation,
        goItemRelation
    );
  }

  public void testInit() {
    EntityLivingBase entity = this.getTestEntity();

    this.slotsHandler.initEquipment(entity);
    verify(this.equipmentModule, times(1)).updateWeaponTrait(
        entity, entity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND), EnumHand.HAND
    );
    verify(this.equipmentModule, times(1)).updateWeaponTrait(
        entity, entity.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND), EnumHand.OFFHAND
    );
    verify(this.equipmentModule, times(1)).updateArmorTrait(
        entity, entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST)
    );
  }
}