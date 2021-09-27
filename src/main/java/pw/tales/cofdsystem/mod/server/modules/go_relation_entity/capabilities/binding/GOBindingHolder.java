package pw.tales.cofdsystem.mod.server.modules.go_relation_entity.capabilities.binding;

import javax.annotation.Nullable;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class GOBindingHolder {

  private String dn = null;

  @SuppressWarnings("java:S1874")
  public static void register() {
    CapabilityManager.INSTANCE
        .register(GOBindingHolder.class, new GOBindingStorage(), GOBindingHolder::new);
  }

  @Nullable
  public String getDN() {
    return this.dn;
  }

  public void setDN(@Nullable String dn) {
    this.dn = dn;
  }
}
