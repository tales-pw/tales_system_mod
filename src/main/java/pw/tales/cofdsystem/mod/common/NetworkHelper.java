package pw.tales.cofdsystem.mod.common;

import net.minecraftforge.fml.relauncher.Side;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.client.modules.equipment.network.handlers.TooltipResponseHandler;
import pw.tales.cofdsystem.mod.client.modules.gui_windows.network.handlers.SystemWindowRemoveHandler;
import pw.tales.cofdsystem.mod.client.modules.gui_windows.network.handlers.SystemWindowUpdateHandler;
import pw.tales.cofdsystem.mod.common.modules.attack.network.mesages.AttackMessage;
import pw.tales.cofdsystem.mod.common.modules.equipment.network.messages.TooltipRequestMessage;
import pw.tales.cofdsystem.mod.common.modules.equipment.network.messages.TooltipResponseMessage;
import pw.tales.cofdsystem.mod.common.modules.go_relation_entity.network.messages.EntityGOBindMessage;
import pw.tales.cofdsystem.mod.common.modules.go_relation_entity.network.messages.EntityGOControlMessage;
import pw.tales.cofdsystem.mod.common.modules.go_relation_entity.network.messages.EntityGOUnloadMessage;
import pw.tales.cofdsystem.mod.common.modules.gui_windows.network.messages.SystemWindowRemoveMessage;
import pw.tales.cofdsystem.mod.common.modules.gui_windows.network.messages.SystemWindowUpdateMessage;
import pw.tales.cofdsystem.mod.common.modules.scene.network.mesages.SceneAddMessage;
import pw.tales.cofdsystem.mod.common.network.messages.SimpleRollMessage;
import pw.tales.cofdsystem.mod.server.modules.attack.network.AttackMessageHandler;
import pw.tales.cofdsystem.mod.server.modules.equipment.network.handlers.TooltipRequestHandler;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.network.handlers.EntityGOBindHandler;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.network.handlers.EntityGOControlHandler;
import pw.tales.cofdsystem.mod.server.modules.go_relation_entity.network.handlers.EntityGOUnloadHandler;
import pw.tales.cofdsystem.mod.server.modules.scene.network.ServerSceneAddHandler;
import pw.tales.cofdsystem.mod.server.modules.simple_roll.network.SimpleRollMessageHandler;

public class NetworkHelper {

  private static final NetworkHelper instance = new NetworkHelper();
  private int packetNumber = 0;

  public static NetworkHelper getInstance() {
    return instance;
  }

  public void initMessageHandlers() {
    TalesSystem.network.registerMessage(
        SimpleRollMessageHandler.class, SimpleRollMessage.class, packetNumber++, Side.SERVER
    );

    TalesSystem.network.registerMessage(
        AttackMessageHandler.class, AttackMessage.class, packetNumber++, Side.SERVER
    );

    TalesSystem.network.registerMessage(
        EntityGOBindHandler.class, EntityGOBindMessage.class, packetNumber++, Side.SERVER
    );

    TalesSystem.network.registerMessage(
        EntityGOUnloadHandler.class, EntityGOUnloadMessage.class, packetNumber++, Side.SERVER
    );

    TalesSystem.network.registerMessage(
        EntityGOControlHandler.class, EntityGOControlMessage.class, packetNumber++, Side.SERVER
    );

    TalesSystem.network.registerMessage(
        ServerSceneAddHandler.class, SceneAddMessage.class, packetNumber++, Side.SERVER
    );

    TalesSystem.network.registerMessage(
        SystemWindowUpdateHandler.class,
        SystemWindowUpdateMessage.class,
        packetNumber++, Side.CLIENT
    );

    TalesSystem.network.registerMessage(
        SystemWindowRemoveHandler.class,
        SystemWindowRemoveMessage.class,
        packetNumber++, Side.CLIENT
    );

    TalesSystem.network.registerMessage(
        TooltipRequestHandler.class,
        TooltipRequestMessage.class,
        packetNumber++, Side.SERVER
    );

    TalesSystem.network.registerMessage(
        TooltipResponseHandler.class,
        TooltipResponseMessage.class,
        packetNumber++, Side.CLIENT
    );
  }
}
