package pw.tales.cofdsystem.mod.server.modules.go_relation_item.exceptions;

import net.minecraft.item.ItemStack;
import pw.tales.cofdsystem.mod.server.modules.go_relation.exceptions.NotBoundException;

public class ItemNotBoundException extends NotBoundException {

  private final ItemStack holder;

  public ItemNotBoundException(ItemStack holder) {
    super(holder);
    this.holder = holder;
  }

  public ItemStack getHolder() {
    return holder;
  }
}
