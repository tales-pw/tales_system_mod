package pw.tales.cofdsystem.mod.server.modules.go_source.events;

import net.minecraftforge.fml.common.eventhandler.Event;
import pw.tales.cofdsystem.game_object.GameObject;

public class GameObjectEvent extends Event {

  private final GameObject gameObject;

  public GameObjectEvent(GameObject gameObject) {
    this.gameObject = gameObject;
  }

  public GameObject getGameObject() {
    return gameObject;
  }
}
