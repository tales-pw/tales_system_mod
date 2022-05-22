package pw.tales.cofdsystem.mod.server.modules.notification.views;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import pw.tales.cofdsystem.character.Character;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.server.views.View;

/**
 * View for GameObject's name.
 */
public class NameView extends View {

  private static final ITextComponent NO_NAME_COMPONENT = new TextComponentTranslation(
      "gameobject.name"
  );

  static {
    Style style = NO_NAME_COMPONENT.getStyle();
    style.setItalic(true);
    style.setColor(TextFormatting.GRAY);
  }

  private final GameObject gameObject;

  public NameView(GameObject gameObject) {
    this.gameObject = gameObject;
  }

  @Override
  public ITextComponent build(ServerPlayerEntity viewer) {
    String name = new Character(this.gameObject).getName();
    if (!name.isEmpty()) {
      return new TextComponentString(name);
    } else {
      return NO_NAME_COMPONENT.createCopy();
    }
  }
}
