package pw.tales.cofdsystem.mod.common.modules.scene.command;

import pw.tales.cofdsystem.mod.common.TalesCommand;


public abstract class SceneNextCommand extends TalesCommand {

  private static final String NAME = "_s.scene.initiative.next";

  protected SceneNextCommand() {
    super(NAME);
  }

  public static String generate(String sceneDn) {
    return String.format("/%s %s", NAME, sceneDn);
  }
}
