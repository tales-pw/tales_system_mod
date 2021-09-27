package pw.tales.cofdsystem.mod.server.modules.operators;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import pw.tales.cofdsystem.mod.server.modules.ServerCommandModule;
import pw.tales.cofdsystem.mod.server.modules.gui_windows.WindowsModule;
import pw.tales.cofdsystem.mod.server.modules.operators.commands.SystemCommand;
import pw.tales.cofdsystem.mod.server.views.View;

/**
 * Module responsible for interaction with system operators.
 */
@Singleton
public class OperatorsModule extends ServerCommandModule {

  public static final String SYSTEM_OPERATOR_PERMISSION = "system_operator";
  public static final String SYSTEM_CONSOLE_OPERATOR_PERMISSION = "system_console_operator";

  private final FMLCommonHandler fmlCommonHandler;
  private final WindowsModule windowsModule;

  @Inject
  public OperatorsModule(
      FMLCommonHandler fmlCommonHandler,
      WindowsModule windowsModule,
      Injector injector
  ) {
    super(injector);
    this.fmlCommonHandler = fmlCommonHandler;
    this.windowsModule = windowsModule;
  }

  @Override
  public Set<Class<? extends CommandBase>> getCommandClasses() {
    return ImmutableSet.of(SystemCommand.class);
  }

  @Override
  public void onServerStart(FMLServerStartingEvent event) {
    super.onServerStart(event);

    PermissionAPI.registerNode(
        SYSTEM_OPERATOR_PERMISSION,
        DefaultPermissionLevel.OP,
        "system operator"
    );

    PermissionAPI.registerNode(
        SYSTEM_CONSOLE_OPERATOR_PERMISSION,
        DefaultPermissionLevel.OP,
        "system console operator"
    );
  }

  /**
   * Show window to all operators.
   *
   * @param view     View.
   * @param windowDn Window identifier.
   */
  public void showWindow(View view, String windowDn) {
    this.getOperators().forEach(e -> this.windowsModule.updateWindow(
        e,
        view,
        windowDn,
        false
    ));
  }

  /**
   * Get all operators online.
   *
   * @return Set of operators.
   */
  public Set<EntityPlayerMP> getOperators() {
    return fmlCommonHandler.getMinecraftServerInstance()
        .getPlayerList()
        .getPlayers()
        .stream()
        .filter(this::isOperator)
        .collect(Collectors.toSet());
  }

  /**
   * Check if command sender is operator.
   *
   * @param commandSender command sender.
   * @return True if operator, False if not or not player.
   */
  public boolean isOperator(ICommandSender commandSender) {
    if (!(commandSender instanceof EntityPlayer)) {
      return false;
    }

    return PermissionAPI.hasPermission(
        (EntityPlayer) commandSender,
        OperatorsModule.SYSTEM_OPERATOR_PERMISSION
    );
  }

  /**
   * Notify operators.
   *
   * @param view View.
   */
  public void notify(View view) {
    this.getOperators().forEach(e -> e.sendMessage(view.build(e)));
  }

  /**
   * Notify operators.
   *
   * @param component Chat component.
   */
  public void notify(ITextComponent component) {
    this.getOperators().forEach(e -> e.sendMessage(component));
  }

  /**
   * Check if command sender has access to system console.
   *
   * @param commandSender command sender.
   * @return True if has access to system console, False if not or not player.
   */
  public boolean isConsoleOperator(ICommandSender commandSender) {
    if (!(commandSender instanceof EntityPlayer)) {
      return false;
    }

    return PermissionAPI.hasPermission(
        (EntityPlayer) commandSender,
        OperatorsModule.SYSTEM_CONSOLE_OPERATOR_PERMISSION
    );
  }
}
