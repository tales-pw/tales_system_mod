package pw.tales.cofdsystem.mod.server.modules.attack.command;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import pw.tales.cofdsystem.action_attack.builder.AttackBuilder;
import pw.tales.cofdsystem.action_attack.builder.EnumResistType;
import pw.tales.cofdsystem.action_attack.builder.EnumSpecifiedTarget;
import pw.tales.cofdsystem.action_attack.builder.exceptions.NoWillpowerBuilderException;
import pw.tales.cofdsystem.common.EnumHand;
import pw.tales.cofdsystem.common.EnumSide;
import pw.tales.cofdsystem.exceptions.CofDSystemException;
import pw.tales.cofdsystem.mod.common.TalesCommand;
import pw.tales.cofdsystem.mod.server.modules.attack.Attack;
import pw.tales.cofdsystem.mod.server.modules.attack.AttackManager;
import pw.tales.cofdsystem.mod.server.modules.attack.AttackNotifications;

public abstract class ConfigureCommand extends TalesCommand {

  protected final EnumSide side;

  protected final AttackManager manager;
  protected final AttackNotifications notifications;

  protected ConfigureCommand(
      String name,
      EnumSide side,
      AttackNotifications notifications,
      AttackManager manager
  ) {
    super(name);
    this.side = side;
    this.notifications = notifications;
    this.manager = manager;
  }

  @Override
  protected void handleSystemException(
      MinecraftServer server,
      ICommandSender sender,
      CofDSystemException systemException
  ) {
    try {
      throw systemException;
    } catch (NoWillpowerBuilderException e) {
      ITextComponent component = new TextComponentTranslation(
          "command.system.error.cant_use_willpower",
          sender.getName()
      );
      this.applyErrorStyle(component);
      sender.sendMessage(component);
    } catch (CofDSystemException e) {
      super.handleSystemException(server, sender, e);
    }
  }

  @Override
  public void wrappedExecute(
      MinecraftServer server,
      ICommandSender sender,
      String[] args
  ) throws CommandException {
    if (args.length < 1) {
      throw new CommandException("command.attack.show.bind.not_enough_arguments");
    }

    UUID uuid = UUID.fromString(args[0]);
    Attack attack = manager.fetch(uuid);

    if (attack == null) {
      throw new CommandException("command.configure.builder_not_found");
    }

    ConfigureAction confAction = ConfigureAction.byName(args[1]);

    if (confAction == null) {
      throw new CommandException(
          "command.configure.unknown_conf_action",
          args[0]
      );
    }

    EnumSide sideRestriction = confAction.getSide();
    if (sideRestriction != null && sideRestriction != this.side) {
      throw new CommandException(
          "command.configure.wrong_side",
          this.side
      );
    }

    AttackBuilder builder = attack.getBuilder();
    switch (confAction) {
      case SET_HAND:
        EnumHand hand = EnumHand.byName(args[2]);
        builder.setHand(this.side, hand);
        break;
      case SET_MODIFIER:
        int modifier = Integer.parseInt(args[2]);
        builder.setModifier(this.side, modifier);
        break;
      case SET_RESIST_TYPE:
        EnumResistType resistType = EnumResistType.byName(args[2]);
        builder.setResist(resistType);
        break;
      case SET_TARGET:
        EnumSpecifiedTarget specTarget = EnumSpecifiedTarget.byName(args[2]);
        if (specTarget == builder.specifiedTarget) {
          specTarget = null;
        }
        builder.setTarget(specTarget);
        break;
      case SET_ALL_OUT:
        builder.setAllOut(!builder.actorAllOut);
        break;
      case SPEND_WILLPOWER:
        builder.spendWillpower(this.side, !builder.actorWillpower);
        break;
      default:
        attack.confirm(this.side);
    }

    if (attack.isBothConfirmed()) {
      this.manager.finish(uuid);
    } else {
      this.notifications.updateSideWindows(attack, this.side, false);
    }
  }

  public enum ConfigureAction {
    SET_HAND("set_hand"),
    SET_ALL_OUT("set_allout", EnumSide.ACTOR),
    SET_RESIST_TYPE("set_resist_type", EnumSide.TARGET),
    SPEND_WILLPOWER("spend_willpower"),
    SET_MODIFIER("set_modifier"),
    SET_TARGET("set_target"),
    CONFIRM("confirm");

    private static final Map<String, ConfigureAction> NAME_MAP = new HashMap<>();

    static {
      for (ConfigureAction value : values()) {
        NAME_MAP.put(value.toString(), value);
      }
    }

    private final String name;
    private final EnumSide side;

    ConfigureAction(String name) {
      this(name, null);
    }

    ConfigureAction(String name, @Nullable EnumSide side) {
      this.name = name;
      this.side = side;
    }

    @Nullable
    public static ConfigureAction byName(String name) {
      return NAME_MAP.getOrDefault(name, null);
    }

    @Override
    public String toString() {
      return this.name;
    }

    @Nullable
    public EnumSide getSide() {
      return side;
    }
  }
}
