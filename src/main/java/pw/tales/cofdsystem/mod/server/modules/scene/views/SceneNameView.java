package pw.tales.cofdsystem.mod.server.modules.scene.views;

import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.common.haxe_adapters.HaxeArrayAdapter;
import pw.tales.cofdsystem.mod.server.views.View;
import pw.tales.cofdsystem.scene.Scene;

public class SceneNameView extends View {

  private final Scene scene;

  public SceneNameView(Scene scene) {
    this.scene = scene;
  }

  public static ITextComponent build(EntityPlayerMP viewer, Scene scene) {
    return new SceneNameView(scene).build(viewer);
  }

  public ITextComponent build(EntityPlayerMP viewer) {
    List<GameObject> order = new HaxeArrayAdapter<>(scene.getInitiative().getOrder());

    ITextComponent sceneComponent;
    if (!order.isEmpty()) {
      String name = order.stream()
          .map(GameObject::getDN)
          .collect(
              Collectors.joining(", ")
          );

      if (name.length() > 10) {
        name = name.substring(0, 10) + "...";
      }

      sceneComponent = new TextComponentString(name);
      sceneComponent.getStyle().setColor(TextFormatting.GREEN);
    } else {
      sceneComponent = new TextComponentTranslation("command.scene.menu.empty");
      sceneComponent.getStyle().setItalic(true);
      sceneComponent.getStyle().setColor(TextFormatting.GRAY);
    }

    ITextComponent uuidComponent = new TextComponentString(scene.getDN());
    sceneComponent.getStyle()
        .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, uuidComponent));

    return sceneComponent;
  }
}
