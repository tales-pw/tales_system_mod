package pw.tales.cofdsystem.mod.server.modules.attack.views;

import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import pw.tales.cofdsystem.action_attack.builder.AttackBuilder;
import pw.tales.cofdsystem.character.Character;
import pw.tales.cofdsystem.common.EnumSide;
import pw.tales.cofdsystem.dices.EnumExplode;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.server.modules.attack.Attack;
import pw.tales.cofdsystem.mod.server.modules.attack.command.AttackShowCommand;
import pw.tales.cofdsystem.mod.server.modules.attack.command.ConfigureCommand.ConfigureAction;
import pw.tales.cofdsystem.mod.server.modules.notification.views.NameView;
import pw.tales.cofdsystem.mod.server.views.ChatActionsBuilder;
import pw.tales.cofdsystem.mod.server.views.TextComponentEmpty;
import pw.tales.cofdsystem.mod.server.views.View;

public abstract class MenuView extends View {

  protected static final EnumExplode[] EXPLODES = {
      EnumExplode.NONE,
      EnumExplode.NINE_AGAIN,
      EnumExplode.EIGHT_AGAIN,
      EnumExplode.ROTE_ACTION
  };

  protected final EnumSide side;
  protected final Attack attack;

  MenuView(Attack attack, EnumSide side) {
    this.attack = attack;
    this.side = side;
  }

  protected ITextComponent buildOpActions(EntityPlayerMP player) {
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
        .add(
            new NameView(target).build(player),
            AttackShowCommand.generate(uuid, EnumSide.TARGET),
            this.side == EnumSide.TARGET
        )
        .build();

    return new TextComponentEmpty()
        .appendText("| ")
        .appendSibling(opActionsComponent)
        .appendText(" |")
        .appendText("\n");
  }

  public ITextComponent buildModifiersComponent() {
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
        .appendText("\n")
        .appendSibling(positiveModifierComponent).appendText("\n")
        .appendSibling(negativeModifierComponent).appendText("\n");
  }

  protected int getModifier() {
    return this.attack.getBuilder().getModifier(this.side);
  }

  public ITextComponent buildExplodeComponent() {
    ChatActionsBuilder builder = new ChatActionsBuilder(
        TextFormatting.GRAY,
        2
    );

    for (EnumExplode explode : EXPLODES) {
      builder.addText(
          String.format("enum.explode.%s", explode.getName()),
          this.generateCommand(ConfigureAction.SET_EXPLODE, explode.getName()),
          this.attack.getBuilder().getExplode(this.side) == explode
      );
    }

    return new TextComponentEmpty()
        .appendText("Режим:").appendText("\n")
        .appendSibling(builder.build())
        .appendText("\n");
  }

  public abstract String generateCommand(Object... arg);
}
