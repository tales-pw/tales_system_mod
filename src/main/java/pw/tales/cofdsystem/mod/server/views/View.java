package pw.tales.cofdsystem.mod.server.views;

import com.google.inject.Inject;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import pw.tales.cofdsystem.mod.server.modules.operators.OperatorsModule;

/**
 * Handles ITextComponent creation for some purpose.
 */
public abstract class View {

  // TODO: maybe find another way to pass operator module?
  //  For now it's easier to just make static inject.
  @Inject
  public static OperatorsModule operatorModule;

  /**
   * Apply common header styles to component.
   *
   * @param component component.
   * @return Component (the same but updated with styles).
   */
  protected ITextComponent applyHeaderStyles(ITextComponent component) {
    component.getStyle()
        .setColor(TextFormatting.BLUE)
        .setBold(true)
        .setUnderlined(true);
    return component.appendText("\n");
  }

  /**
   * Check if viewer is operator.
   *
   * @param viewer Viewer.
   * @return True if they are operator, False if not or is null.
   */
  public boolean isOperator(@Nullable EntityPlayerMP viewer) {
    if (viewer == null) {
      return false;
    }

    return operatorModule.isOperator(viewer);
  }

  /**
   * Builds final text component.
   *
   * @param viewer Somebody who will see result message.
   * @return ITextComponent.
   */
  public abstract ITextComponent build(EntityPlayerMP viewer);
}
