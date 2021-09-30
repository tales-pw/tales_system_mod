package pw.tales.cofdsystem.mod.server.modules.equipment.command;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import pw.tales.cofdsystem.mod.common.TalesCommand;

public abstract class BindCommand extends TalesCommand {

  protected BindCommand(String name) {
    super(name);
  }

  /**
   * Disable default weapon stats display.
   *
   * @param itemStack item stack.
   */
  protected static void disableDefaultTooltip(ItemStack itemStack) {
    NBTTagCompound nbt = itemStack.getTagCompound();

    if (nbt == null) {
      nbt = new NBTTagCompound();
    }

    nbt.setInteger("HideFlags", 63);

    itemStack.setTagCompound(nbt);
  }
}
