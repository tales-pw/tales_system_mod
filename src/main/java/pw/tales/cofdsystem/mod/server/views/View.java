package pw.tales.cofdsystem.mod.server.views;

import com.google.inject.Inject;
import javax.annotation.Nullable;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
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

  /**
   * Build header component.
   *
   * @param text Header text.
   * @return Header component.
   */
  protected ITextComponent buildHeader(String text) {
    TextComponentString header = new TextComponentString(text);
    this.applyHeaderStyles(header);
    return header;
  }

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
   * Build component with label and value.
   *
   * @param key   Label text.
   * @param value Integer value.
   * @return component
   */
  protected ITextComponent buildKV(String key, int value) {
    return this.buildKV(key, Integer.toString(value));
  }

  /**
   * Build component with label and value.
   *
   * @param key   Label text.
   * @param value String value.
   * @return component
   */
  protected ITextComponent buildKV(String key, String value) {
    ITextComponent keyComponent = this.buildKVLabel(key);

    ITextComponent valueComponent = new TextComponentTranslation(value);
    valueComponent.getStyle().setColor(TextFormatting.GRAY);

    return this.joinKV(keyComponent, valueComponent);
  }

  private ITextComponent buildKVLabel(String key) {
    ITextComponent statName = new TextComponentTranslation(key);
    statName.getStyle().setColor(TextFormatting.YELLOW);
    return statName;
  }

  private ITextComponent joinKV(
      ITextComponent keyComponent,
      ITextComponent valueComponent
  ) {
    return keyComponent.appendText(": ").appendSibling(valueComponent);
  }

  public ITextComponent applyErrorStyle(ITextComponent component) {
    component.getStyle().setColor(TextFormatting.RED);
    return component;
  }

  /**
   * Build component with label and value.
   *
   * @param key           Label text.
   * @param commandSender Command sender (probably entity).
   * @return component
   */
  protected ITextComponent buildKV(String key, ICommandSender commandSender) {
    ITextComponent keyComponent = this.buildKVLabel(key);

    ITextComponent valueComponent = commandSender.getDisplayName();
    valueComponent.getStyle().setColor(TextFormatting.GRAY);

    return this.joinKV(keyComponent, valueComponent);
  }
}
