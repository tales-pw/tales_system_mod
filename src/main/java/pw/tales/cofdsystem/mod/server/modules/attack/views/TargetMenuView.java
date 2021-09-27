package pw.tales.cofdsystem.mod.server.modules.attack.views;

import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import pw.tales.cofdsystem.action_attack.builder.AttackBuilder;
import pw.tales.cofdsystem.action_attack.builder.EnumResistType;
import pw.tales.cofdsystem.common.EnumSide;
import pw.tales.cofdsystem.mod.server.modules.attack.AttackManager;
import pw.tales.cofdsystem.mod.server.modules.attack.command.ConfigureCommand;
import pw.tales.cofdsystem.mod.server.modules.attack.command.ConfigureCommand.ConfigureAction;
import pw.tales.cofdsystem.mod.server.modules.attack.command.ConfigureTargetCommand;
import pw.tales.cofdsystem.mod.server.views.ChatActionsBuilder;
import pw.tales.cofdsystem.mod.server.views.TextComponentEmpty;

public class TargetMenuView extends MenuView {

  private final AttackManager attackManager;

  public TargetMenuView(UUID uuid,
      AttackManager attackManager,
      AttackBuilder attack
  ) {
    super(uuid, attack, EnumSide.TARGET);
    this.attackManager = attackManager;
  }

  @Override
  public int getModifier() {
    return this.attack.targetModifier;
  }

  public ITextComponent build(EntityPlayerMP viewer) {
    // Main Header
    ITextComponent header = new TextComponentString("Защита");
    header.getStyle().setBold(true).setColor(TextFormatting.DARK_AQUA);

    ITextComponent othersComponent = new ChatActionsBuilder(TextFormatting.GRAY)
        .addText(
            "Сила Воли",
            this.generateCommand(ConfigureAction.SPEND_WILLPOWER, null),
            attack.targetWillpower
        )
        .build();

    ITextComponent okComponent = new ChatActionsBuilder(TextFormatting.GRAY)
        .addText(
            "OK",
            this.generateCommand(ConfigureAction.CONFIRM, null),
            this.attackManager.isConfirmed(this.uuid, EnumSide.TARGET)
        )
        .build();

    // Output
    TextComponentString root = new TextComponentEmpty();
    root.getStyle().setColor(TextFormatting.GOLD);
    return root
        .appendSibling(this.buildOpActions(viewer))
        .appendText("\n").appendText("\n")
        .appendSibling(header).appendText("\n")
        .appendSibling(this.buildDefenceComponent())
        .appendSibling(this.buildModifiersComponent())
        .appendText("Другое: ").appendSibling(othersComponent).appendText("\n")
        .appendText("Завершить: ").appendSibling(okComponent);
  }

  private ITextComponent buildDefenceComponent() {
    // Resist types
    ITextComponent resistTypesComponent = new ChatActionsBuilder(TextFormatting.GRAY)
        .addText(
            "Обычная защита",
            this.generateCommand(ConfigureAction.SET_RESIST_TYPE, EnumResistType.DEFAULT),
            this.attack.targetResistType == EnumResistType.DEFAULT
        )
        .addText(
            "Уворот",
            this.generateCommand(ConfigureAction.SET_RESIST_TYPE, EnumResistType.DODGE),
            this.attack.targetResistType == EnumResistType.DODGE
        )
        .addText(
            "Не защищаться",
            this.generateCommand(ConfigureAction.SET_RESIST_TYPE, EnumResistType.NO_DEFENCE),
            this.attack.targetResistType == EnumResistType.NO_DEFENCE
        )
        .build();

    return new TextComponentString("Тип: ")
        .appendSibling(resistTypesComponent)
        .appendText("\n");
  }

  @Override
  public String generateCommand(Object... args) {
    ConfigureCommand.ConfigureAction action = (ConfigureCommand.ConfigureAction) args[0];
    return ConfigureTargetCommand.generate(uuid, action, args[1]);
  }
}
