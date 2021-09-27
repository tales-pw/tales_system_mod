package pw.tales.cofdsystem.mod.server.modules.go_relation.exceptions;

public abstract class NotBoundException extends RuntimeException {

  private final transient Object holder;

  protected NotBoundException(Object holder) {
    this.holder = holder;
  }

  public Object getHolder() {
    return holder;
  }
}
