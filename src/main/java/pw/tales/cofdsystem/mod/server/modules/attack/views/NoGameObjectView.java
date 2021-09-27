package pw.tales.cofdsystem.mod.server.modules.attack.views;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import pw.tales.cofdsystem.mod.server.views.View;

public class NoGameObjectView extends View {

  private final Entity mcAttacker;

  public NoGameObjectView(Entity mcAttacker) {
    this.mcAttacker = mcAttacker;
  }

  @Override
  public ITextComponent build(EntityPlayerMP viewer) {
    ITextComponent components = new TextComponentTranslation(
        "chat.menu.attack.no_game_object", mcAttacker.toString()
    );
    components.getStyle().setColor(TextFormatting.RED);
    return components;
  }
}
