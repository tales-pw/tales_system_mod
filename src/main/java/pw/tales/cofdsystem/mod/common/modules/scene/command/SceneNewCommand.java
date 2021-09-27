package pw.tales.cofdsystem.mod.common.modules.scene.command;

import pw.tales.cofdsystem.mod.common.TalesCommand;


public abstract class SceneNewCommand extends TalesCommand {

  private static final String NAME = "_s.scene.new";

  protected SceneNewCommand() {
    super(NAME);
  }

  public static String generate() {
    return String.format("/%s", NAME);
  }
}
