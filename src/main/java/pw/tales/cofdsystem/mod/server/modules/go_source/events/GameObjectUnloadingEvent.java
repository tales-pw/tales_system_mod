package pw.tales.cofdsystem.mod.server.modules.go_source.events;

import pw.tales.cofdsystem.game_object.GameObject;

public abstract class GameObjectUnloadingEvent extends GameObjectEvent {

  GameObjectUnloadingEvent(GameObject gameObject) {
    super(gameObject);
  }

  public static class PRE extends GameObjectUnloadingEvent {

    public PRE(GameObject gameObject) {
      super(gameObject);
    }
  }

  public static class POST extends GameObjectUnloadingEvent {

    public POST(GameObject gameObject) {
      super(gameObject);
    }
  }
}
