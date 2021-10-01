package pw.tales.cofdsystem.mod.server.modules.attack.views;

import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import pw.tales.cofdsystem.action_attack.builder.AttackBuilder;
import pw.tales.cofdsystem.action_attack.builder.EnumResistType;
import pw.tales.cofdsystem.common.EnumSide;
import pw.tales.cofdsystem.mod.server.modules.attack.Attack;
import pw.tales.cofdsystem.mod.server.modules.attack.command.ConfigureCommand;
import pw.tales.cofdsystem.mod.server.modules.attack.command.ConfigureCommand.ConfigureAction;
import pw.tales.cofdsystem.mod.server.modules.attack.command.ConfigureTargetCommand;
import pw.tales.cofdsystem.mod.server.views.ChatActionsBuilder;
import pw.tales.cofdsystem.mod.server.views.TextComponentEmpty;

public class TargetMenuView extends MenuView {

  public TargetMenuView(Attack attack) {
    super(attack, EnumSide.TARGET);
  }

  @Override
  public int getModifier() {
    AttackBuilder builder = this.attack.getBuilder();
    return builder.targetModifier;
  }

  public ITextComponent build(EntityPlayerMP viewer) {
    AttackBuilder builder = this.attack.getBuilder();

    // Main Header
    ITextComponent header = new TextComponentString("Защита");
    header.getStyle().setBold(true).setColor(TextFormatting.DARK_AQUA);

    ITextComponent othersComponent = new ChatActionsBuilder(TextFormatting.GRAY)
        .addText(
            "Сила Воли",
            this.generateCommand(ConfigureAction.SPEND_WILLPOWER, null),
            builder.targetWillpower
        )
        .build();

    ITextComponent okComponent = new ChatActionsBuilder(TextFormatting.GRAY)
        .addText(
            "OK",
            this.generateCommand(ConfigureAction.CONFIRM, null),
            this.attack.isConfirmed(EnumSide.TARGET)
        )
        .build();

    // Output
    TextComponentString root = new TextComponentEmpty();
    root.getStyle().setColor(TextFormatting.GOLD);
    return root
        .appendSibling(this.buildOpInfo(viewer))
        .appendText("\n").appendText("\n")
        .appendSibling(header).appendText("\n")
        .appendSibling(this.buildDefenceComponent())
        .appendSibling(this.buildModifiersComponent())
        .appendText("Другое: ").appendSibling(othersComponent).appendText("\n")
        .appendText("Завершить: ").appendSibling(okComponent);
  }

  private ITextComponent buildDefenceComponent() {
    AttackBuilder builder = this.attack.getBuilder();

    // Resist types
    ITextComponent resistTypesComponent = new ChatActionsBuilder(TextFormatting.GRAY)
        .addText(
            "Обычная защита",
            this.generateCommand(ConfigureAction.SET_RESIST_TYPE, EnumResistType.DEFAULT),
            builder.targetResistType == EnumResistType.DEFAULT
        )
        .addText(
            "Уворот",
            this.generateCommand(ConfigureAction.SET_RESIST_TYPE, EnumResistType.DODGE),
            builder.targetResistType == EnumResistType.DODGE
        )
        .addText(
            "Не защищаться",
            this.generateCommand(ConfigureAction.SET_RESIST_TYPE, EnumResistType.NO_DEFENCE),
            builder.targetResistType == EnumResistType.NO_DEFENCE
        )
        .build();

    return new TextComponentString("Тип: ")
        .appendSibling(resistTypesComponent)
        .appendText("\n");
  }

  @Override
  public String generateCommand(Object... args) {
    UUID uuid = this.attack.getId();
    ConfigureCommand.ConfigureAction action = (ConfigureCommand.ConfigureAction) args[0];
    return ConfigureTargetCommand.generate(uuid, action, args[1]);
  }
}
