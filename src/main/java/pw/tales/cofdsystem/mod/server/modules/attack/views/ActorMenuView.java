package pw.tales.cofdsystem.mod.server.modules.attack.views;

import static pw.tales.cofdsystem.common.EnumHand.HAND;
import static pw.tales.cofdsystem.common.EnumHand.OFFHAND;

import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import pw.tales.cofdsystem.action_attack.builder.AttackBuilder;
import pw.tales.cofdsystem.action_attack.builder.EnumSpecifiedTarget;
import pw.tales.cofdsystem.common.EnumSide;
import pw.tales.cofdsystem.mod.common.haxe_adapters.HaxeMapAdapter;
import pw.tales.cofdsystem.mod.server.modules.attack.Attack;
import pw.tales.cofdsystem.mod.server.modules.attack.command.ConfigureActorCommand;
import pw.tales.cofdsystem.mod.server.modules.attack.command.ConfigureCommand.ConfigureAction;
import pw.tales.cofdsystem.mod.server.views.ChatActionsBuilder;
import pw.tales.cofdsystem.mod.server.views.TextComponentEmpty;

public class ActorMenuView extends MenuView {

  public static final HaxeMapAdapter<EnumSpecifiedTarget> TARGETS = new HaxeMapAdapter<>(
      EnumSpecifiedTarget.VALUES);

  public ActorMenuView(Attack attack) {
    super(attack, EnumSide.ACTOR);
  }

  public ITextComponent build(EntityPlayerMP viewer) {
    // Main Header
    ITextComponent header = new TextComponentString("Атака");
    header.getStyle().setBold(true).setColor(TextFormatting.DARK_AQUA);

    AttackBuilder builder = this.attack.getBuilder();

    ITextComponent othersComponent = new ChatActionsBuilder(TextFormatting.GRAY)
        .addText(
            "Безрассудная атака",
            this.generateCommand(ConfigureAction.SET_ALL_OUT, null),
            builder.isAllOut()
        )
        .addText(
            "Сила Воли",
            this.generateCommand(ConfigureAction.SPEND_WILLPOWER, null),
            builder.getSpendWillpower(this.side)
        )
        .build();

    ITextComponent okComponent = new ChatActionsBuilder(TextFormatting.GRAY)
        .addText(
            "OK",
            this.generateCommand(ConfigureAction.CONFIRM, null),
            this.attack.isConfirmed(this.side)
        )
        .build();

    // Output
    TextComponentString component = new TextComponentEmpty();
    component.getStyle().setColor(TextFormatting.GOLD);

    if (isOperator(viewer)) {
      component.appendSibling(this.buildOpActions(viewer));
    }

    return component
        .appendSibling(header).appendText("\n")
        .appendSibling(this.buildHandComponent())
        .appendSibling(this.buildSpecifiedTarget())
        .appendSibling(this.buildModifiersComponent())
        .appendSibling(this.buildExplodeComponent())
        .appendText("Другое: ").appendSibling(othersComponent).appendText("\n")
        .appendText("Завершить: ").appendSibling(okComponent);
  }

  @Override
  public String generateCommand(Object... args) {
    UUID uuid = this.attack.getId();
    ConfigureAction attackAction = (ConfigureAction) args[0];
    return ConfigureActorCommand.generate(uuid, attackAction, args[1]);
  }

  private ITextComponent buildHandComponent() {
    UUID uuid = this.attack.getId();
    AttackBuilder builder = this.attack.getBuilder();

    ITextComponent handComponent = new ChatActionsBuilder(TextFormatting.GRAY)
        .addText(
            "Основной рукой",
            ConfigureActorCommand.generate(
                uuid,
                ConfigureAction.SET_HAND,
                HAND
            ),
            builder.getHand(this.side) == HAND
        )
        .addText(
            "Второй рукой",
            ConfigureActorCommand.generate(
                uuid,
                ConfigureAction.SET_HAND,
                OFFHAND
            ),
            builder.getHand(this.side) == OFFHAND
        )
        .build();
    return new TextComponentString("Рука: ").appendSibling(handComponent).appendText("\n");
  }

  private ITextComponent buildSpecifiedTarget() {
    // Specified target
    ChatActionsBuilder specifiedTarget = new ChatActionsBuilder(TextFormatting.GRAY);

    UUID uuid = attack.getId();
    AttackBuilder builder = this.attack.getBuilder();

    for (EnumSpecifiedTarget target : TARGETS.values()) {
      specifiedTarget = specifiedTarget.addText(
          String.format("target.%s", target.toString()),
          ConfigureActorCommand.generate(uuid, ConfigureAction.SET_TARGET, target),
          builder.getSpecifiedTarget() == target
      );
    }

    return new TextComponentString("Точечная атака:").appendText("\n")
        .appendSibling(specifiedTarget.build()).appendText("\n");
  }

}
