package pw.tales.cofdsystem.mod.common.modules.scene.command;

import pw.tales.cofdsystem.mod.common.TalesCommand;


public abstract class SceneJoinCommand extends TalesCommand {

  private static final String NAME = "_s.scene.join";

  protected SceneJoinCommand() {
    super(NAME);
  }

  public static String generate() {
    return String.format("/%s", NAME);
  }
}
