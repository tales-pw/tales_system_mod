package pw.tales.cofdsystem.mod.server.modules.go_source.events;

import pw.tales.cofdsystem.game_object.GameObject;

public class GameObjectLoadedEvent extends GameObjectEvent {

  public GameObjectLoadedEvent(GameObject gameObject) {
    super(gameObject);
  }
}
