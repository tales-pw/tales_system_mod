package pw.tales.cofdsystem.mod.server.modules.action_roll.views;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import pw.tales.cofdsystem.mod.server.views.View;

public class HeaderView extends View {

  private final ITextComponent component;

  public HeaderView(ITextComponent component) {
    this.component = component;
  }

  public ITextComponent build(ServerPlayerEntity viewer) {
    return this.applyHeaderStyles(this.component);
  }
}
