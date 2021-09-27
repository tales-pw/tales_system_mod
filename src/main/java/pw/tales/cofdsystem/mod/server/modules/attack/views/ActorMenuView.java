package pw.tales.cofdsystem.mod.server.modules.attack.views;

import static pw.tales.cofdsystem.common.EnumHand.HAND;
import static pw.tales.cofdsystem.common.EnumHand.OFFHAND;

import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import pw.tales.cofdsystem.action_attack.builder.AttackBuilder;
import pw.tales.cofdsystem.action_attack.builder.EnumSpecifiedTarget;
import pw.tales.cofdsystem.common.EnumSide;
import pw.tales.cofdsystem.mod.common.haxe_adapters.HaxeMapAdapter;
import pw.tales.cofdsystem.mod.server.modules.attack.AttackManager;
import pw.tales.cofdsystem.mod.server.modules.attack.command.ConfigureActorCommand;
import pw.tales.cofdsystem.mod.server.modules.attack.command.ConfigureCommand.ConfigureAction;
import pw.tales.cofdsystem.mod.server.views.ChatActionsBuilder;
import pw.tales.cofdsystem.mod.server.views.TextComponentEmpty;

public class ActorMenuView extends MenuView {

  public static final HaxeMapAdapter<EnumSpecifiedTarget> TARGETS = new HaxeMapAdapter<>(
      EnumSpecifiedTarget.VALUES);

  private final AttackManager attackManager;

  public ActorMenuView(
      UUID uuid,
      AttackManager attackManager,
      AttackBuilder attack
  ) {
    super(uuid, attack, EnumSide.ACTOR);
    this.attackManager = attackManager;
  }

  @Override
  public int getModifier() {
    return this.attack.actorModifier;
  }

  public ITextComponent build(EntityPlayerMP viewer) {
    // Main Header
    ITextComponent header = new TextComponentString("Атака");
    header.getStyle().setBold(true).setColor(TextFormatting.DARK_AQUA);

    ITextComponent othersComponent = new ChatActionsBuilder(TextFormatting.GRAY)
        .addText(
            "Безрассудная атака",
            this.generateCommand(ConfigureAction.SET_ALL_OUT, null),
            this.attack.actorAllOut
        )
        .addText(
            "Сила Воли",
            this.generateCommand(ConfigureAction.SPEND_WILLPOWER, null),
            this.attack.actorWillpower
        )
        .build();

    ITextComponent okComponent = new ChatActionsBuilder(TextFormatting.GRAY)
        .addText(
            "OK",
            this.generateCommand(ConfigureAction.CONFIRM, null),
            this.attackManager.isConfirmed(this.uuid, EnumSide.ACTOR)
        )
        .build();

    // Output
    TextComponentString root = new TextComponentEmpty();
    root.getStyle().setColor(TextFormatting.GOLD);

    return root
        .appendSibling(this.buildOpActions(viewer))
        .appendText("\n").appendText("\n")
        .appendSibling(header).appendText("\n")
        .appendSibling(this.buildHandComponent())
        .appendSibling(this.buildSpecifiedTarget())
        .appendSibling(this.buildModifiersComponent())
        .appendText("Другое: ").appendSibling(othersComponent).appendText("\n")
        .appendText("Завершить: ").appendSibling(okComponent);
  }

  @Override
  public String generateCommand(Object... args) {
    ConfigureAction attackAction = (ConfigureAction) args[0];
    return ConfigureActorCommand.generate(uuid, attackAction, args[1]);
  }

  private ITextComponent buildHandComponent() {
    ITextComponent handComponent = new ChatActionsBuilder(TextFormatting.GRAY)
        .addText(
            "Основной рукой",
            ConfigureActorCommand.generate(uuid, ConfigureAction.SET_HAND, HAND),
            this.attack.actorHand == HAND
        )
        .addText(
            "Второй рукой",
            ConfigureActorCommand.generate(uuid, ConfigureAction.SET_HAND, OFFHAND),
            this.attack.actorHand == OFFHAND
        )
        .build();
    return new TextComponentString("Рука: ").appendSibling(handComponent).appendText("\n");
  }

  private ITextComponent buildSpecifiedTarget() {
    // Specified target
    ChatActionsBuilder specifiedTarget = new ChatActionsBuilder(TextFormatting.GRAY);

    for (EnumSpecifiedTarget target : TARGETS.values()) {
      specifiedTarget = specifiedTarget.add(
          new TextComponentTranslation(
              String.format("target.%s", target.toString())
          ),
          ConfigureActorCommand.generate(uuid, ConfigureAction.SET_TARGET, target),
          this.attack.specifiedTarget == target
      );
    }

    return new TextComponentString("Точечная атака:").appendText("\n")
        .appendSibling(specifiedTarget.build()).appendText("\n");
  }

}
