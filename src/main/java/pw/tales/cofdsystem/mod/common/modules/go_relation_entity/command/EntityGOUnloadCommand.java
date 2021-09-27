package pw.tales.cofdsystem.mod.common.modules.go_relation_entity.command;

import java.util.UUID;
import pw.tales.cofdsystem.mod.common.TalesCommand;


public abstract class EntityGOUnloadCommand extends TalesCommand {

  private static final String NAME = "s.go.entity.unload";

  protected EntityGOUnloadCommand() {
    super(NAME);
  }

  public static String generate(UUID uuid) {
    return String.format("/%s %s", NAME, uuid);
  }
}