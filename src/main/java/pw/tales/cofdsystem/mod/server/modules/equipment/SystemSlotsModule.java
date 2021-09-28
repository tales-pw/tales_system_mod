package pw.tales.cofdsystem.mod.server.modules.equipment;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.common.IModule;
import pw.tales.cofdsystem.mod.server.modules.equipment.system_slots.ArmorSlot;
import pw.tales.cofdsystem.mod.server.modules.equipment.system_slots.MainHandSlot;
import pw.tales.cofdsystem.mod.server.modules.equipment.system_slots.OffHandSlot;
import pw.tales.cofdsystem.mod.server.modules.equipment.system_slots.SystemSlot;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.GOEntityRelation;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.events.GameObjectAttachedEvent;

@Singleton
public class SystemSlotsModule implements IModule {

  private final List<SystemSlot> slots = new ArrayList<>();
  private final GOEntityRelation goEntityRelation;

  @Inject
  public SystemSlotsModule(
      EquipmentModule equipmentModule,
      GOEntityRelation goEntityRelation
  ) {
    this.goEntityRelation = goEntityRelation;

    slots.add(new MainHandSlot(equipmentModule));
    slots.add(new OffHandSlot(equipmentModule));
    slots.add(new ArmorSlot(equipmentModule));
  }

  @SubscribeEvent
  public void onLivingEquipmentChangeEvent(LivingEquipmentChangeEvent event) {
    EntityLivingBase entity = event.getEntityLiving();

    if (!this.goEntityRelation.isBound(entity)) {
      return;
    }

    slots.forEach(slot -> slot.update(
        entity,
        event.getSlot(),
        event.getTo()
    ));
  }

  @SubscribeEvent
  public void onGameObjectAttachedEvent(GameObjectAttachedEvent event) {
    Entity entity = event.getEntity();

    if (!(entity instanceof EntityLivingBase)) {
      return;
    }

    if (!this.goEntityRelation.isBound(entity)) {
      return;
    }

    TalesSystem.logger.debug(
        "Initialize {} equipment from {}.",
        event.getGameObject(),
        entity
    );

    this.initEquipment((EntityLivingBase) entity);
  }

  public void initEquipment(EntityLivingBase entity) {
    slots.forEach(slot -> slot.init(entity));
  }

  public void setUp() {
    EVENT_BUS.register(this);
  }
}
