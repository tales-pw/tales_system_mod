package pw.tales.cofdsystem.mod.common.modules.scene.command;

import pw.tales.cofdsystem.mod.common.TalesCommand;


public abstract class SceneBindCommand extends TalesCommand {

  private static final String NAME = "_s.scene.bind";

  protected SceneBindCommand() {
    super(NAME);
  }

  public static String generate(String dn) {
    return String.format("/%s %s", NAME, dn);
  }
}
