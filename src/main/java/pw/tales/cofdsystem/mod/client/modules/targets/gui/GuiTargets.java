package pw.tales.cofdsystem.mod.client.modules.targets.gui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.client.modules.targets.TargetsList;
import pw.tales.cofdsystem.mod.client.modules.targets.TargetsModule;
import pw.tales.cofdsystem.mod.client.projection.ProjectionHelper;

@Singleton
public class GuiTargets extends Gui {

  private static final ResourceLocation SELECT_MARK_TEXTURES = new ResourceLocation(
      TalesSystem.MOD_ID, "textures/gui/select_mark.png"
  );
  private final ProjectionHelper projection;
  private final TargetsList targets;
  private final Minecraft minecraft;

  private boolean debug = false;

  @Inject
  public GuiTargets(
      Minecraft minecraft,
      ProjectionHelper projection,
      TargetsList targets
  ) {
    this.minecraft = minecraft;
    this.projection = projection;
    this.targets = targets;
  }

  public void mouseClicked(int mouseX, int mouseY) {
    Vec3d mouse = new Vec3d(mouseX, mouseY, 0);

    for (Entity entity : this.minecraft.world.loadedEntityList) {
      if (entity.equals(this.minecraft.player)) {
        continue;
      }

      if (projection.isEntityClicked(mouse, entity)) {
        this.selectEntity(entity);
        return;
      }
    }
  }

  private void selectEntity(Entity entity) {
    if (isShiftKeyDown()) {
      if (this.targets.hasEntity(entity)) {
        this.targets.removeEntity(entity);
        return;
      }
    } else {
      this.targets.clear();
    }

    this.targets.addEntity(entity);
  }

  public static boolean isShiftKeyDown() {
    return Keyboard.isKeyDown(TargetsModule.multipleTargetsKey.getKeyCode());
  }

  @SubscribeEvent
  public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
    if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
      return;
    }
    this.render();
  }

  public void render() {
    if (this.debug) {
      this.drawDebug();
    }
    this.drawSelectedMarks();
  }

  private void drawSelectedMarks() {
    for (Entity entity : this.targets.getLoadedEntities()) {
      this.drawSelectMark(entity);
    }
  }

  private void drawSelectMark(Entity entity) {
    Vec3d vec3d;

    if (entity instanceof EntityLivingBase) {
      vec3d = projection.transformGlobalPoint(
          entity.getPositionEyes(this.minecraft.getRenderPartialTicks())
      );
    } else {
      vec3d = projection.transformGlobalPoint(entity.getPositionVector());
    }

    // Entity is not on the screen
    if (vec3d == null) {
      return;
    }

    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    GlStateManager.disableDepth();
    GlStateManager.enableAlpha();

    minecraft.getTextureManager().bindTexture(SELECT_MARK_TEXTURES);

    int size = 64;

    drawModalRectWithCustomSizedTexture(
        (int) vec3d.x - size / 2,
        (int) vec3d.y - size / 2,
        0, 0, size, size, size, size
    );

    GlStateManager.disableAlpha();
    GlStateManager.enableDepth();
  }

  private void drawDebug() {
    List<Entity> entities = new ArrayList<>();
    for (Entity entity1 : this.minecraft.world.loadedEntityList) {
      if (!entity1.equals(this.minecraft.player)) {
        entities.add(entity1);
      }
    }

    for (Entity entity : entities) {
      Vec3d vec3d = projection.transformGlobalPoint(entity.getPositionVector());
      AxisAlignedBB[] collisions = projection.transformEntity(entity);

      for (AxisAlignedBB collision : collisions) {
        drawRect(
            (int) collision.minX,
            (int) collision.minY,
            (int) collision.maxX,
            (int) collision.maxY,
            Color.GRAY.getRGB()
        );
      }

      if (vec3d == null) {
        continue;
      }

      this.drawString(
          minecraft.fontRenderer,
          entity.toString(),
          (int) vec3d.x,
          (int) vec3d.y,
          0xFF00FF00
      );
    }
  }

  @SubscribeEvent
  public void onKeyboardEvent(InputEvent.KeyInputEvent event) {
    if (TargetsModule.debugKey.isPressed()) {
      this.debug = !debug;
    }
  }
}
