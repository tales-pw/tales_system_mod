package pw.tales.cofdsystem.mod.common.modules.scene.command;

import pw.tales.cofdsystem.mod.common.TalesCommand;


public abstract class SceneBeginCommand extends TalesCommand {

  private static final String NAME = "_s.scene.begin";

  protected SceneBeginCommand() {
    super(NAME);
  }

  public static String generate() {
    return SceneBeginCommand.generate(false);
  }

  public static String generate(boolean confirm) {
    return String.format("/%s %s", NAME, confirm ? "true" : "false");
  }
}
