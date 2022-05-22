package pw.tales.cofdsystem.mod.server.modules.scene.network;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.server.permission.PermissionAPI;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.common.modules.scene.network.mesages.SceneAddMessage;
import pw.tales.cofdsystem.mod.common.network.TalesMessageHandler;
import pw.tales.cofdsystem.mod.server.modules.action_roll.views.HeaderView;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.GOEntityRelation;
import pw.tales.cofdsystem.mod.server.modules.notification.views.NameView;
import pw.tales.cofdsystem.mod.server.modules.operators.OperatorsModule;
import pw.tales.cofdsystem.mod.server.modules.scene.SceneModule;
import pw.tales.cofdsystem.mod.server.views.TextComponentEmpty;
import pw.tales.cofdsystem.scene.Scene;

public class ServerSceneAddHandler extends TalesMessageHandler<SceneAddMessage> {

  @SuppressWarnings({"java:S1444", "java:S1104"})
  @Inject
  public static SceneModule sceneModule;

  @SuppressWarnings({"java:S1444", "java:S1104"})
  @Inject
  public static GOEntityRelation goEntityRelation;

  @Override
  public boolean checkPermission(MinecraftServer server, ServerPlayerEntity player) {
    return PermissionAPI.hasPermission(
        player,
        OperatorsModule.SYSTEM_OPERATOR_PERMISSION
    );
  }

  @Override
  public void process(ServerPlayerEntity player, SceneAddMessage message) throws CommandException {
    Entity[] targets = message.getEntities();

    if (targets.length == 0) {
      throw new CommandException("command.scene.add.empty_targets");
    }

    Scene scene = sceneModule.getBoundScene(player);
    if (scene == null) {
      throw new CommandException("command.scene.menu.bound.no");
    }

    // Adding GameObjects to scene
    Map<String, ITextComponent> errors = new HashMap<>();
    List<CompletableFuture<GameObject>> futures = new ArrayList<>();

    for (Entity target : targets) {
      futures.add(this.addEntityToScene(scene, target, errors));
    }

    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenAccept((aVoid -> {
      // Output to user
      TextComponentEmpty component = new TextComponentEmpty();
      component.appendText("\n");

      // Add header
      ITextComponent headerComponent = new HeaderView(
          new TextComponentTranslation("command.scene.add.header")
      ).build(player);
      component.appendSibling(headerComponent);

      // TODO: Avoid blocking join when fetching errors and successes.
      // List errors and successes
      int i = 1;
      // Add successes
      for (CompletableFuture<GameObject> future : futures) {
        GameObject gameObject = future.join();

        if (gameObject == null) {
          continue;
        }

        component
            .appendText(Integer.toString(i)).appendText(") ")
            .appendSibling(this.buildSuccess(player, gameObject)).appendText("\n");

        i++;
      }

      // Add errors
      for (Map.Entry<String, ITextComponent> entry : errors.entrySet()) {
        component
            .appendText(Integer.toString(i)).appendText(") ")
            .appendSibling(this.buildError(entry)).appendText("\n");

        i++;
      }

      player.sendMessage(component);
    })).exceptionally(e -> {
      this.handleErrors(player, e);
      return null;
    });
  }

  @Nullable
  public CompletableFuture<GameObject> addEntityToScene(Scene scene, Entity entity,
      Map<String, ITextComponent> errors) {
    return goEntityRelation.getGameObject(entity)
        .thenApply((go -> {
          scene.add(go);
          return go;
        })).exceptionally(e -> {
          TextComponentTranslation component = new TextComponentTranslation(
              "command.scene.add.entity_has_no_game_object");
          component.getStyle().setHoverEvent(new HoverEvent(
              HoverEvent.Action.SHOW_TEXT,
              entity.getDisplayName()
          ));

          errors.put(entity.getPersistentID().toString(), component);
          return null;
        });
  }

  public ITextComponent buildError(Map.Entry<String, ITextComponent> entry) {
    ITextComponent gameObjectComponent = new TextComponentEmpty();
    Style style = gameObjectComponent.getStyle();
    style.setColor(TextFormatting.RED);

    if (style.getHoverEvent() == null) {
      style.setHoverEvent(new HoverEvent(
          HoverEvent.Action.SHOW_TEXT,
          new TextComponentString(entry.getKey())
      ));
    }

    gameObjectComponent.appendSibling(entry.getValue());
    return gameObjectComponent;
  }

  public ITextComponent buildSuccess(ServerPlayerEntity viewer, GameObject gameObject) {
    ITextComponent gameObjectComponent = new NameView(gameObject).build(viewer);
    gameObjectComponent.getStyle().setColor(TextFormatting.GREEN);
    gameObjectComponent.appendText(": ").appendSibling(
        new TextComponentTranslation("command.scene.add.item_success")
    );

    return gameObjectComponent;
  }
}
