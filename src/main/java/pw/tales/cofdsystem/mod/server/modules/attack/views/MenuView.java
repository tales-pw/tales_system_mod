package pw.tales.cofdsystem.mod.server.modules.attack.views;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import pw.tales.cofdsystem.action_attack.builder.AttackBuilder;
import pw.tales.cofdsystem.character.Character;
import pw.tales.cofdsystem.common.EnumSide;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.server.modules.attack.Attack;
import pw.tales.cofdsystem.mod.server.modules.attack.command.AttackShowCommand;
import pw.tales.cofdsystem.mod.server.modules.attack.command.ConfigureCommand.ConfigureAction;
import pw.tales.cofdsystem.mod.server.views.ChatActionsBuilder;
import pw.tales.cofdsystem.mod.server.views.TextComponentEmpty;
import pw.tales.cofdsystem.mod.server.views.View;

public abstract class MenuView extends View {

  protected final EnumSide side;
  protected final Attack attack;

  MenuView(Attack attack, EnumSide side) {
    this.attack = attack;
    this.side = side;
  }

  protected ITextComponent buildOpActions(@Nullable EntityPlayerMP viewer) {
    if (!isOperator(viewer)) {
      return new TextComponentEmpty();
    }

    UUID uuid = this.attack.getId();
    AttackBuilder builder = this.attack.getBuilder();

    GameObject actor = builder.getActor();
    GameObject target = builder.getTarget();

    ITextComponent opActionsComponent = new ChatActionsBuilder(TextFormatting.GRAY)
        .addText(
            new Character(actor).getName(),
            AttackShowCommand.generate(uuid, EnumSide.ACTOR),
            this.side == EnumSide.ACTOR
        )
        .addText(
            new Character(target).getName(),
            AttackShowCommand.generate(uuid, EnumSide.TARGET),
            this.side == EnumSide.TARGET
        )
        .build();

    return new TextComponentString("| ")
        .appendSibling(opActionsComponent)
        .appendText(" |");
  }

  public ITextComponent buildModifiersComponent() {
    // Reset custom modifiers
    ITextComponent resetModifierComponent = new ChatActionsBuilder(TextFormatting.GRAY)
        .addText(
            "отменить",
            this.generateCommand(ConfigureAction.SET_MODIFIER, 0)
        )
        .build();

    // Positive custom modifiers
    ChatActionsBuilder positiveModifierBuilder = new ChatActionsBuilder(TextFormatting.GRAY, 10);
    for (int i = 1; i <= 10; i++) {
      positiveModifierBuilder.addText(
          String.format("+%d", i),
          this.generateCommand(ConfigureAction.SET_MODIFIER, i),
          this.getModifier() == i
      );
    }
    ITextComponent positiveModifierComponent = positiveModifierBuilder.build();

    // Negative custom modifiers
    ChatActionsBuilder negativeModifierBuilder = new ChatActionsBuilder(TextFormatting.GRAY, 10);
    for (int i = -1; i >= -10; i--) {
      negativeModifierBuilder.addText(
          String.valueOf(i),
          this.generateCommand(ConfigureAction.SET_MODIFIER, i),
          this.getModifier() == i
      );
    }
    ITextComponent negativeModifierComponent = negativeModifierBuilder.build();

    return new TextComponentString("Дополнительный модификатор ")
        .appendSibling(resetModifierComponent).appendText("\n")
        .appendSibling(positiveModifierComponent).appendText("\n")
        .appendSibling(negativeModifierComponent).appendText("\n");
  }

  public abstract String generateCommand(Object... arg);

  public abstract int getModifier();
}
