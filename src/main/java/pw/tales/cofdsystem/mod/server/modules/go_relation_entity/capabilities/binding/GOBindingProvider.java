package pw.tales.cofdsystem.mod.server.modules.go_relation_entity.capabilities.binding;

import javax.annotation.Nullable;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class GOBindingProvider implements ICapabilitySerializable<NBTBase> {

  @SuppressWarnings({"java:S1444", "java:S3008"})
  @CapabilityInject(GOBindingHolder.class)
  public static Capability<GOBindingHolder> GO_BINDING_CAPABILITY = null;

  private final GOBindingHolder instance = GO_BINDING_CAPABILITY.getDefaultInstance();

  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    return capability == GO_BINDING_CAPABILITY;
  }

  @Nullable
  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
    return capability == GO_BINDING_CAPABILITY ? GO_BINDING_CAPABILITY.cast(this.instance)
        : null;
  }

  @Override
  public NBTBase serializeNBT() {
    return GO_BINDING_CAPABILITY.getStorage()
        .writeNBT(GO_BINDING_CAPABILITY, this.instance, null);
  }

  @Override
  public void deserializeNBT(NBTBase nbt) {
    GO_BINDING_CAPABILITY.getStorage().readNBT(GO_BINDING_CAPABILITY, this.instance, null, nbt);
  }
}