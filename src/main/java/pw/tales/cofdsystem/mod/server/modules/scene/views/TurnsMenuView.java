package pw.tales.cofdsystem.mod.server.modules.scene.views;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.event.HoverEvent.Action;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.common.haxe_adapters.HaxeArrayAdapter;
import pw.tales.cofdsystem.mod.common.modules.scene.command.SceneNextCommand;
import pw.tales.cofdsystem.mod.server.modules.notification.views.NameView;
import pw.tales.cofdsystem.mod.server.views.ChatActionsBuilder;
import pw.tales.cofdsystem.mod.server.views.TextComponentEmpty;
import pw.tales.cofdsystem.scene.Scene;
import pw.tales.cofdsystem.scene.initiative.Initiative;
import pw.tales.cofdsystem.scene.turns.Turns;

public class TurnsMenuView extends SceneView {

  private final GameObject gameObject;
  private final Scene scene;

  public TurnsMenuView(Scene scene, @Nullable GameObject gameObject) {
    super(scene);
    this.scene = scene;
    this.gameObject = gameObject;
  }

  @Override
  public ITextComponent build(@Nullable ServerPlayerEntity viewer) {
    return new TextComponentEmpty()
        .appendSibling(this.buildHeader())
        .appendSibling(this.buildTurnOrder(viewer))
        .appendText("\n")
        .appendText("\n")
        .appendSibling(this.buildActions(viewer));
  }

  private ITextComponent buildHeader() {
    ITextComponent component = new TextComponentTranslation("command.scene.initiative.header");
    return this.applyHeaderStyles(component);
  }

  private ITextComponent buildActions(@Nullable ServerPlayerEntity viewer) {
    ChatActionsBuilder initiativeActions = new ChatActionsBuilder(TextFormatting.GRAY);

    boolean viewerTurn = this.scene.getTurns().getTurn() == this.gameObject;
    boolean viewerOp = this.isOperator(viewer);

    if (viewerOp || viewerTurn) {
      initiativeActions.add(
          new TextComponentTranslation("command.scene.initiative.next"),
          SceneNextCommand.generate(this.scene.getDN())
      );
    }

    return initiativeActions.build();
  }

  private ITextComponent buildTurnOrder(@Nullable ServerPlayerEntity viewer) {
    Initiative initiative = this.scene.getInitiative();
    Turns turns = this.scene.getTurns();

    List<GameObject> order = new HaxeArrayAdapter<>(initiative.getOrder());
    if (order.isEmpty()) {
      TextComponentTranslation emptyComponent = new TextComponentTranslation(
          "command.scene.initiative.empty");
      emptyComponent.getStyle().setColor(TextFormatting.GRAY);
      return emptyComponent;
    }

    ITextComponent component = new TextComponentEmpty();
    int i = 1;
    for (GameObject otherGameObject : order) {
      component.appendSibling(this.buildInitiativeElement(
          viewer,
          otherGameObject,
          turns.getTurn() == otherGameObject
      ));
      if (i < order.size()) {
        component.appendText(", ");
      }
      i++;
    }

    return component;
  }

  private ITextComponent buildInitiativeElement(
      @Nullable ServerPlayerEntity viewer,
      GameObject gameObject,
      boolean currentTurn
  ) {
    ITextComponent component = new NameView(gameObject).build(viewer);
    if (currentTurn) {
      component.getStyle().setUnderlined(true).setColor(TextFormatting.WHITE);
    } else {
      component.getStyle().setColor(TextFormatting.GRAY);
    }

    Initiative order = this.scene.getInitiative();

    if (this.isOperator(viewer)) {
      HoverEvent hoverEvent = new HoverEvent(Action.SHOW_TEXT,
          new TextComponentString(gameObject.getDN())
      );
      component.getStyle().setHoverEvent(hoverEvent);

      component = component.appendText(" (").appendText(
          String.valueOf(order.getInitiative(gameObject))
      ).appendText(")");
    }

    return component;
  }
}
