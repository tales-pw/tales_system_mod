package pw.tales.cofdsystem.mod.common.modules.scene.command;

import pw.tales.cofdsystem.mod.common.TalesCommand;


public abstract class SceneAddSelectedCommand extends TalesCommand {

  public static final String NAME = "_s.scene.add.selected";

  protected SceneAddSelectedCommand() {
    super(NAME);
  }

  public static String generate() {
    return String.format("/%s", NAME);
  }
}
