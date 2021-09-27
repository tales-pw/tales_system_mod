package pw.tales.cofdsystem.mod.client.projection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.glu.Project;

@SuppressWarnings({"java:S2696", "java:S3011", "java:S1104", "java:S1444"})
@Singleton
public class ProjectionHelper {

  public IntBuffer viewport;
  public FloatBuffer modelview;
  public FloatBuffer projection;

  @Inject
  public ProjectionHelper() {
    // Nothing to take as parameter.
  }

  public boolean isEntityClicked(Vec3d mouse, Entity entity) {
    AxisAlignedBB[] collisions = this.transformEntity(entity);

    for (AxisAlignedBB collision : collisions) {
      if (collision.contains(mouse)) {
        return true;
      }
    }

    return false;
  }

  public AxisAlignedBB[] transformEntity(Entity entity) {
    AxisAlignedBB c = entity.getEntityBoundingBox();

    // Plane 1
    Vec3d pos11 = new Vec3d(c.minX, c.minY, c.minZ);
    pos11 = this.transformGlobalPoint(pos11);
    if (pos11 == null) {
      return new AxisAlignedBB[0];
    }

    Vec3d pos12 = new Vec3d(c.maxX, c.maxY, c.maxZ);
    pos12 = this.transformGlobalPoint(pos12);
    if (pos12 == null) {
      return new AxisAlignedBB[0];
    }

    // Plane 2
    Vec3d pos21 = new Vec3d(c.minX, c.minY, c.maxZ);
    pos21 = this.transformGlobalPoint(pos21);
    if (pos21 == null) {
      return new AxisAlignedBB[0];
    }

    Vec3d pos22 = new Vec3d(c.maxX, c.maxY, c.minZ);
    pos22 = this.transformGlobalPoint(pos22);
    if (pos22 == null) {
      return new AxisAlignedBB[0];
    }

    AxisAlignedBB[] collisions = new AxisAlignedBB[2];
    collisions[0] = new AxisAlignedBB(
        pos11.x, pos11.y, 1,
        pos12.x, pos12.y, -1
    );
    collisions[1] = new AxisAlignedBB(
        pos21.x, pos21.y, 1,
        pos22.x, pos22.y, -1
    );

    return collisions;
  }

  @Nullable
  public Vec3d transformGlobalPoint(Vec3d pos) {
    Minecraft mc = Minecraft.getMinecraft();
    final ScaledResolution scaledresolution = new ScaledResolution(mc);

    Vec3d relativeEntityPos = pos.subtract(this.getRenderViewEntityPos());
    Vec3d vec3d = this.transformRelativePoint(relativeEntityPos);

    // Entity is not on the screen
    if (vec3d.z > 1) {
      return null;
    }

    vec3d = vec3d.scale(1f / scaledresolution.getScaleFactor());

    return new Vec3d(vec3d.x,
        ((float) mc.displayHeight / scaledresolution.getScaleFactor()) - vec3d.y, vec3d.z);
  }

  public Vec3d transformRelativePoint(Vec3d pos) {
    FloatBuffer screenCoords = BufferUtils.createFloatBuffer(16);

    updateRenderInfo();
    Project.gluProject(
        (float) pos.x, (float) pos.y, (float) pos.z,
        modelview, projection, viewport,
        screenCoords
    );

    return new Vec3d(screenCoords.get(0), screenCoords.get(1), screenCoords.get(2));
  }

  private void updateRenderInfo() {
    viewport = ObfuscationReflectionHelper.getPrivateValue(
        ActiveRenderInfo.class,
        null,
        "field_178814_a"
    ); // VIEWPORT
    modelview = ObfuscationReflectionHelper.getPrivateValue(
        ActiveRenderInfo.class,
        null,
        "field_178812_b"
    ); // MODELVIEW
    projection = ObfuscationReflectionHelper.getPrivateValue(
        ActiveRenderInfo.class,
        null,
        "field_178813_c"
    ); // PROJECTION
  }

  private Vec3d getRenderViewEntityPos() {
    Minecraft mc = Minecraft.getMinecraft();
    Entity entity = mc.getRenderViewEntity();
    float partialTicks = mc.getRenderPartialTicks();

    Objects.requireNonNull(entity);

    double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks;
    double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks;
    double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;

    return new Vec3d(d0, d1, d2);
  }
}
