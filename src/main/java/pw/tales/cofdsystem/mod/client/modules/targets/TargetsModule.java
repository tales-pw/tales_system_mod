package pw.tales.cofdsystem.mod.client.modules.targets;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;
import pw.tales.cofdsystem.mod.client.modules.targets.gui.GuiTargets;
import pw.tales.cofdsystem.mod.common.IModule;

@Singleton
public class TargetsModule implements IModule {

  public static final KeyBinding multipleTargetsKey = new KeyBinding(
      "key.multiple_targets.desc",
      Keyboard.KEY_LSHIFT,
      "key.tales_system.category"
  );

  public static final KeyBinding debugKey = new KeyBinding(
      "key.tales_debug.desc",
      Keyboard.KEY_INSERT,
      "key.tales_system.category"
  );

  private final GuiTargets guiTargets;

  @Inject
  public TargetsModule(GuiTargets guiTargets) {
    this.guiTargets = guiTargets;
  }

  @Override
  public void setUp() {
    ClientRegistry.registerKeyBinding(multipleTargetsKey);
    ClientRegistry.registerKeyBinding(debugKey);
    EVENT_BUS.register(this.guiTargets);
  }
}
