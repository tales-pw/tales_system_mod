package pw.tales.cofdsystem.mod.common.errors.handlers;

import com.google.inject.Singleton;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

@Singleton
public class DefaultErrorHandler extends BaseErrorHandler<Exception> {

  public DefaultErrorHandler() {
    super(Exception.class);
  }

  @Override
  public void handleError(ICommandSender recipient, Exception e) {
    e.printStackTrace();

    ITextComponent component = new TextComponentTranslation(
        "pw.tales.mod.error.generic"
    );
    component.getStyle().setColor(TextFormatting.RED);

    recipient.sendMessage(component);
  }
}
