package pw.tales.cofdsystem.mod.server.modules.scene.views;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import pw.tales.cofdsystem.mod.server.modules.scene.exceptions.NoSceneViewException;
import pw.tales.cofdsystem.mod.server.views.View;
import pw.tales.cofdsystem.scene.Scene;

public abstract class SceneView extends View {

  protected Scene scene;

  protected SceneView(@Nullable Scene scene) {
    this.scene = scene;
  }

  public ITextComponent buildSceneName(EntityPlayerMP viewer) {
    if (this.scene == null) {
      throw new NoSceneViewException();
    }

    return SceneNameView.build(viewer, this.scene);
  }
}
