package pw.tales.cofdsystem.mod.server.modules.scene.views;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import pw.tales.cofdsystem.mod.common.modules.scene.command.SceneAddSelectedCommand;
import pw.tales.cofdsystem.mod.common.modules.scene.command.SceneJoinCommand;
import pw.tales.cofdsystem.mod.common.modules.scene.command.SceneListCommand;
import pw.tales.cofdsystem.mod.server.modules.scene.command.ServerSceneBeginCommand;
import pw.tales.cofdsystem.mod.server.modules.scene.command.ServerSceneEndCommand;
import pw.tales.cofdsystem.mod.server.modules.scene.command.ServerSceneNewCommand;
import pw.tales.cofdsystem.mod.server.modules.scene.command.ServerSceneShowCommand;
import pw.tales.cofdsystem.mod.server.views.ChatActionsBuilder;
import pw.tales.cofdsystem.mod.server.views.TextComponentEmpty;
import pw.tales.cofdsystem.scene.Scene;

public class SceneMenuView extends SceneView {

  public SceneMenuView(@Nullable Scene scene) {
    super(scene);
  }

  public ITextComponent build(EntityPlayerMP viewer) {
    return new TextComponentEmpty()
        .appendSibling(this.buildHeader())
        .appendSibling(
            this.buildSceneView(viewer, scene)
        ).appendText("\n")
        .appendText("\n")
        .appendSibling(this.buildActions(scene));
  }

  private ITextComponent buildHeader() {
    ITextComponent component = new TextComponentTranslation("command.scene.menu.header");
    return this.applyHeaderStyles(component);
  }

  private ITextComponent buildSceneView(EntityPlayerMP viewer, @Nullable Scene scene) {
    if (scene == null) {
      return new TextComponentTranslation("command.scene.menu.bound.no");
    }

    ITextComponent sceneComponent = this.buildSceneName(viewer);
    return new TextComponentTranslation("command.scene_menu.bound_scene")
        .appendText(" ")
        .appendSibling(sceneComponent);
  }

  private ITextComponent buildActions(@Nullable Scene scene) {
    TextComponentString components = new TextComponentString("");

    // Actions to choose scene to control
    ChatActionsBuilder builderChooseActions = new ChatActionsBuilder(TextFormatting.GRAY);
    builderChooseActions.add(
        new TextComponentTranslation("command.scene.menu.create"),
        ServerSceneNewCommand.generate()
    );
    builderChooseActions.add(
        new TextComponentTranslation("command.scene.menu.list"),
        SceneListCommand.generate()
    );
    components
        .appendSibling(new TextComponentTranslation("command.scene.menu.scenes_actions.label"))
        .appendText(" ")
        .appendSibling(builderChooseActions.build());

    if (scene != null) {
      // Actions to add and delete members
      ChatActionsBuilder membersActionsBuilder = new ChatActionsBuilder(TextFormatting.GRAY);
      membersActionsBuilder.add(
          new TextComponentTranslation("command.scene.menu.add"),
          SceneAddSelectedCommand.generate()
      );
      membersActionsBuilder.add(
          new TextComponentTranslation("command.scene.menu.join"),
          SceneJoinCommand.generate()
      );

      components.appendText("\n")
          .appendSibling(new TextComponentTranslation("command.scene.menu.memebers_actions.label"))
          .appendText(" ")
          .appendSibling(membersActionsBuilder.build());

      // Actions to change or check scene state
      ChatActionsBuilder stateActionsBuilder = new ChatActionsBuilder(TextFormatting.GRAY);
      stateActionsBuilder.add(
          new TextComponentTranslation("command.scene.menu.begin"),
          ServerSceneBeginCommand.generate()
      );
      stateActionsBuilder.add(
          new TextComponentTranslation("command.scene.menu.show"),
          ServerSceneShowCommand.generate()
      );
      stateActionsBuilder.add(
          new TextComponentTranslation("command.scene.menu.end"),
          ServerSceneEndCommand.generate()
      );

      components.appendText("\n")
          .appendSibling(new TextComponentTranslation("command.scene.menu.state_actions.label"))
          .appendText(" ")
          .appendSibling(stateActionsBuilder.build());
    }

    return components;
  }
}
