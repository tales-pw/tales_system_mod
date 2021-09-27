package pw.tales.cofdsystem.mod.server.modules.go_relation_entity.capabilities.binding;

import javax.annotation.Nullable;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class GOBindingStorage implements Capability.IStorage<GOBindingHolder> {

  @Nullable
  @Override
  public NBTBase writeNBT(Capability<GOBindingHolder> capability, GOBindingHolder instance,
      EnumFacing side) {
    NBTTagCompound nbt = new NBTTagCompound();

    if (instance.getDN() != null) {
      nbt.setTag("dn", new NBTTagString(instance.getDN()));
    }

    return nbt;
  }

  @Override
  public void readNBT(Capability<GOBindingHolder> capability, GOBindingHolder instance,
      EnumFacing side, NBTBase nbt) {
    NBTTagCompound nbt1 = (NBTTagCompound) nbt;
    if (nbt1.hasKey("dn")) {
      instance.setDN(nbt1.getString("dn"));
    }

  }
}
