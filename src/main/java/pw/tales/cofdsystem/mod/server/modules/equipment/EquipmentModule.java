package pw.tales.cofdsystem.mod.server.modules.equipment;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.action.IAction;
import pw.tales.cofdsystem.action.events.ActionPerformedEvent;
import pw.tales.cofdsystem.armor.Armor;
import pw.tales.cofdsystem.armor.actions.DonAction;
import pw.tales.cofdsystem.armor.traits.armor_rating.ArmorRating;
import pw.tales.cofdsystem.character.traits.HeldWeapon;
import pw.tales.cofdsystem.character.traits.WornArmor;
import pw.tales.cofdsystem.common.EnumHand;
import pw.tales.cofdsystem.equipment.Equipment;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.Utils;
import pw.tales.cofdsystem.mod.common.haxe_adapters.HaxeFn;
import pw.tales.cofdsystem.mod.server.modules.ServerCommandModule;
import pw.tales.cofdsystem.mod.server.modules.equipment.command.BindArmorCommand;
import pw.tales.cofdsystem.mod.server.modules.equipment.command.BindWeaponCommand;
import pw.tales.cofdsystem.mod.server.modules.equipment.rpchat_messages.ArmorMessage;
import pw.tales.cofdsystem.mod.server.modules.equipment.rpchat_messages.HandItemMessage;
import pw.tales.cofdsystem.mod.server.modules.go_relation.exceptions.NotBoundException;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.GOEntityRelation;
import pw.tales.cofdsystem.mod.server.modules.go_relation_item.GOItemRelation;
import pw.tales.cofdsystem.mod.server.modules.notification.NotificationModule;
import pw.tales.cofdsystem.utils.events.HandlerPriority;
import pw.tales.cofdsystem.weapon.Weapon;
import pw.tales.cofdsystem.weapon.actions.PickAction;
import pw.tales.cofdsystem.weapon.traits.DamageMod;

@Singleton
public class EquipmentModule extends ServerCommandModule {

  private final CofDSystem system;
  private final GOItemRelation goItemRelation;
  private final GOEntityRelation goEntityRelation;
  private final NotificationModule notificationModule;

  @Inject
  public EquipmentModule(
      CofDSystem system,
      GOItemRelation goItemRelation,
      GOEntityRelation goEntityRelation,
      NotificationModule notificationModule,
      Injector injector
  ) {
    super(injector);
    this.system = system;
    this.goItemRelation = goItemRelation;
    this.goEntityRelation = goEntityRelation;
    this.notificationModule = notificationModule;
  }

  @Override
  public void setUp() {
    EVENT_BUS.register(this);

    this.system.events.addHandler(ActionPerformedEvent.class, HaxeFn.wrap(
        this::onItemActionPerformed
    ), HandlerPriority.NORMAL);

    this.system.events.addHandler(ActionPerformedEvent.class, HaxeFn.wrap(
        this::onArmorActionPerformed
    ), HandlerPriority.NORMAL);
  }

  private void onItemActionPerformed(ActionPerformedEvent event) {
    IAction action = event.action;

    if (!(action instanceof PickAction)) {
      return;
    }

    PickAction pickAction = (PickAction) action;
    GameObject actor = pickAction.actor;

    this.notificationModule.sendGOAsOrigin(new HandItemMessage(
        pickAction.actor,
        pickAction.weapon,
        pickAction.hand
    ), actor);
  }

  private void onArmorActionPerformed(ActionPerformedEvent event) {
    IAction action = event.action;

    if (!(action instanceof DonAction)) {
      return;
    }

    DonAction pickAction = (DonAction) action;
    GameObject actor = pickAction.actor;

    this.notificationModule.sendGOAsOrigin(new ArmorMessage(
        pickAction.actor,
        pickAction.armor
    ), actor);
  }

  /**
   * Performs action to don armor.
   *
   * @param entity    Entity for which to find GameObject and perform action.
   * @param itemStack ItemStack from which take armor.
   */
  public void performActionDonArmor(Entity entity, ItemStack itemStack) {
    Utils.merge(
        this.goEntityRelation.getGameObject(entity),
        this.getArmor(itemStack)
    ).thenAccept(pair -> {
      GameObject gameObject = pair.getKey();
      Armor armor = pair.getValue();

      this.performActionDonArmor(gameObject, armor);
    }).exceptionally(e -> this.logError(entity, e));
  }

  public CompletableFuture<Armor> getArmor(ItemStack itemStack) {
    return wrapEquipCreation(
        this.goItemRelation.getGameObject(itemStack).thenApply(Armor::new).thenApply(
            armor -> {
              // Don't treat something without armor rating as armor
              GameObject gameObject = armor.getGameObject();
              ArmorRating rating = gameObject.getTrait(ArmorRating.TYPE, null);

              if (rating == null) {
                return null;
              }

              return armor;
            }
        )
    );
  }

  private static <T extends Equipment> CompletableFuture<T> wrapEquipCreation(
      CompletableFuture<T> future
  ) {
    return Utils.ignoreExc(future, NotBoundException.class);
  }

  public void performActionDonArmor(
      GameObject gameObject,
      @Nullable Armor newArmor
  ) {
    WornArmor trait = gameObject.getTrait(WornArmor.TYPE, null);
    @Nullable Armor oldArmor = trait.getArmor();

    if (Objects.equals(oldArmor, newArmor)) {
      return;
    }

    TalesSystem.logger.debug(
        "Changing {} armor from {} to {}.",
        gameObject,
        oldArmor,
        newArmor
    );

    this.system.act(new DonAction(gameObject, newArmor, system));
  }

  private Void logError(Entity entity, Throwable throwable) {
    TextComponentTranslation component = new TextComponentTranslation(
        "equipment.change.error",
        throwable.getMessage()
    );
    component.getStyle().setColor(TextFormatting.RED);
    entity.sendMessage(component);
    throwable.printStackTrace();
    throw new CompletionException(throwable);
  }

  /**
   * Similar to performActionDonArmor but instead of performing action, sets armor directly to
   * trait. Useful for initialization.
   *
   * @param entity    Entity for which to find GameObject and set armor.
   * @param itemStack ItemStack from which take armor.
   */
  public void updateArmorTrait(Entity entity, ItemStack itemStack) {
    Utils.merge(
        this.goEntityRelation.getGameObject(entity),
        this.getArmor(itemStack)
    ).thenAccept(pair -> {
      GameObject gameObject = pair.getKey();
      Armor armor = pair.getValue();
      TalesSystem.logger.debug(
          "Updating armor for {}, {}, and {}.",
          gameObject,
          entity,
          armor
      );
      WornArmor trait = gameObject.getTrait(WornArmor.TYPE, null);
      trait.setArmor(armor);
    }).exceptionally(e -> this.logError(entity, e));
  }

  /**
   * Performs action to draw or sheath weapon.
   *
   * @param entity    Entity for which to find GameObject and perform action.
   * @param itemStack ItemStack from which take weapon.
   */
  public void performActionPickItem(
      Entity entity,
      ItemStack itemStack,
      EnumHand hand
  ) {
    Utils.merge(
        this.goEntityRelation.getGameObject(entity),
        this.getWeapon(itemStack)
    ).thenAccept(pair -> {
      GameObject gameObject = pair.getKey();
      Weapon newWeapon = pair.getValue();
      this.performActionPickItem(gameObject, newWeapon, hand);
    }).exceptionally(e -> this.logError(entity, e));
  }

  public CompletableFuture<Weapon> getWeapon(ItemStack itemStack) {
    return wrapEquipCreation(
        this.goItemRelation.getGameObject(itemStack).thenApply(Weapon::new).thenApply(
            weapon -> {

              if (!this.shouldTreatAsWeapon(weapon)) {
                return null;
              }

              return weapon;
            }
        )
    );
  }

  public boolean shouldTreatAsWeapon(Weapon weapon) {
    // Don't treat something without weapon rating as weapon
    GameObject gameObject = weapon.getGameObject();
    DamageMod damageMod = gameObject.getTrait(DamageMod.TYPE, null);
    return damageMod != null;
  }

  public void performActionPickItem(
      GameObject gameObject,
      @Nullable Weapon newWeapon,
      EnumHand hand
  ) {
    HeldWeapon trait = gameObject.getTrait(HeldWeapon.TYPE, null);
    Weapon oldWeapon = trait.getHand(hand);

    if (Objects.equals(oldWeapon, newWeapon)) {
      return;
    }

    TalesSystem.logger.debug(
        "Changing weapon for {} in {} from {} to {}.",
        gameObject,
        hand,
        oldWeapon,
        newWeapon
    );

    this.system.act(new PickAction(gameObject, hand, newWeapon, system));
  }

  /**
   * Similar to performActionPickItem but instead of performing action, sets weapon directly to
   * trait. Useful for initialization.
   *
   * @param entity    Entity for which to find GameObject and set weapon.
   * @param itemStack ItemStack from which take weapon.
   */
  public void updateWeaponTrait(Entity entity, ItemStack itemStack, EnumHand hand) {
    Utils.merge(
        this.goEntityRelation.getGameObject(entity),
        this.getWeapon(itemStack)
    ).thenAccept(pair -> {
      GameObject gameObject = pair.getKey();
      Weapon weapon = pair.getValue();
      TalesSystem.logger.debug(
          "Updating weapon for {}, {}, {} and {}.",
          gameObject,
          entity,
          hand,
          weapon
      );
      HeldWeapon trait = gameObject.getTrait(HeldWeapon.TYPE, null);
      trait.setHand(hand, weapon);
    }).exceptionally(e -> this.logError(entity, e));
  }

  @Override
  public Set<Class<? extends CommandBase>> getCommandClasses() {
    return ImmutableSet.of(
        BindArmorCommand.class,
        BindWeaponCommand.class
    );
  }
}
