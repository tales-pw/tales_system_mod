package pw.tales.cofdsystem.mod.server.modules.scene.views;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import pw.tales.cofdsystem.mod.common.haxe_adapters.HaxeArrayAdapter;
import pw.tales.cofdsystem.mod.common.modules.scene.command.SceneBindCommand;
import pw.tales.cofdsystem.mod.server.modules.scene.SceneModule;
import pw.tales.cofdsystem.mod.server.views.TextComponentEmpty;
import pw.tales.cofdsystem.mod.server.views.View;
import pw.tales.cofdsystem.scene.Scene;

public class SceneListView extends View {

  private final SceneModule initiativeModule;

  public SceneListView(SceneModule sceneModule) {
    this.initiativeModule = sceneModule;
  }

  @Override
  public ITextComponent build(EntityPlayerMP viewer) {
    List<Scene> scenes = new HaxeArrayAdapter<>(initiativeModule.getSceneRegistry().items());

    return new TextComponentEmpty()
        .appendSibling(this.buildHeader())
        .appendSibling(this.buildSceneListView(viewer, scenes));
  }

  public ITextComponent buildHeader() {
    ITextComponent component = new TextComponentTranslation("command.scene.list.header");
    component.getStyle()
        .setColor(TextFormatting.BLUE)
        .setBold(true)
        .setUnderlined(true);
    component = component.appendText("\n");
    return component;
  }

  public ITextComponent buildSceneListView(EntityPlayerMP viewer, List<Scene> scenes) {
    if (scenes.isEmpty()) {
      ITextComponent emptyComponent = new TextComponentTranslation("command.scene.list.empty");
      emptyComponent.getStyle().setColor(TextFormatting.GRAY).setItalic(true);
      return emptyComponent;
    }

    ITextComponent listComponent = new TextComponentEmpty();

    int i = 1;
    for (Scene scene : scenes) {
      ITextComponent component = new TextComponentEmpty();
      if (i != 1) {
        component.appendText("\n");
      }
      component.appendText(i + ") ");
      component.appendSibling(SceneNameView.build(viewer, scene));

      component.getStyle().setClickEvent(new ClickEvent(
          ClickEvent.Action.RUN_COMMAND,
          SceneBindCommand.generate(scene.getDN())
      ));

      listComponent.appendSibling(component);
      i++;
    }

    return listComponent;
  }
}
