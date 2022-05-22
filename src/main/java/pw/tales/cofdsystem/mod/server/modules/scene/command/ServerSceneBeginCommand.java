package pw.tales.cofdsystem.mod.server.modules.scene.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.common.haxe_adapters.HaxeArrayAdapter;
import pw.tales.cofdsystem.mod.common.modules.scene.command.SceneBeginCommand;
import pw.tales.cofdsystem.mod.server.modules.operators.OperatorsModule;
import pw.tales.cofdsystem.mod.server.modules.scene.SceneModule;
import pw.tales.cofdsystem.mod.server.views.TextComponentEmpty;
import pw.tales.cofdsystem.scene.Scene;
import pw.tales.cofdsystem.scene.initiative.Initiative;


@Singleton
public class ServerSceneBeginCommand extends SceneBeginCommand {

  private final OperatorsModule operatorsModule;
  private final SceneModule sceneModule;

  @Inject
  public ServerSceneBeginCommand(
      OperatorsModule operatorsModule,
      SceneModule sceneModule
  ) {
    this.operatorsModule = operatorsModule;
    this.sceneModule = sceneModule;
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return this.operatorsModule.isOperator(sender);
  }

  @Override
  public void wrappedExecute(
      MinecraftServer server,
      ICommandSender sender,
      String[] args
  ) throws CommandException {
    ServerPlayerEntity entity = getCommandSenderAsPlayer(sender);

    Scene scene = this.sceneModule.getBoundScene(entity);
    if (scene == null) {
      throw new CommandException("command.scene.menu.bound.no");
    }

    Initiative initiative = scene.getInitiative();

    // Request confirmation if scene is empty
    boolean confirmed = CommandBase.parseBoolean(args[0]);

    List<GameObject> order = new HaxeArrayAdapter<>(initiative.getOrder());
    if (order.isEmpty() && !confirmed) {
      entity.sendMessage(this.buildRequestConfirmView());
      return;
    }

    // Start scene
    scene.begin();
    scene.getTurns().start();

    TalesSystem.logger.info("{} is started by {}.", scene, entity.getName());
  }

  public ITextComponent buildRequestConfirmView() {
    // Request text
    TextComponentTranslation textComponent = new TextComponentTranslation(
        "command.scene.begin.request_confirm");
    textComponent.getStyle().setColor(TextFormatting.YELLOW);

    // Confirm button component
    ITextComponent confirmComponent = new TextComponentString("[")
        .appendSibling(new TextComponentTranslation(
            "command.scene.begin.request_confirm.confirm"
        )).appendText("]");

    confirmComponent.getStyle().setClickEvent(
        new ClickEvent(ClickEvent.Action.RUN_COMMAND, ServerSceneBeginCommand.generate(true))
    ).setColor(TextFormatting.GRAY);

    // Build final message
    return new TextComponentEmpty()
        .appendSibling(textComponent)
        .appendText("\n")
        .appendSibling(confirmComponent);
  }
}
