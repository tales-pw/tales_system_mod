package pw.tales.cofdsystem.mod.server.modules.equipment;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

  private static final Set<Class<? extends SystemSlot>> SLOT_CLASSES = ImmutableSet.of(
      MainHandSlot.class,
      OffHandSlot.class,
      ArmorSlot.class
  );

  private final List<SystemSlot> slots;
  private final GOEntityRelation goEntityRelation;

  @Inject
  public SystemSlotsModule(
      Injector injector,
      GOEntityRelation goEntityRelation
  ) {
    this.goEntityRelation = goEntityRelation;
    this.slots = SLOT_CLASSES.stream()
        .map(injector::getInstance)
        .collect(Collectors.toList());
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
