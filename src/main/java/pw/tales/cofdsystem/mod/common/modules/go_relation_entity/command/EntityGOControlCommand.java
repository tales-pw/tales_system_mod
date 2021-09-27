package pw.tales.cofdsystem.mod.common.modules.go_relation_entity.command;

import java.util.UUID;
import pw.tales.cofdsystem.mod.common.TalesCommand;


public abstract class EntityGOControlCommand extends TalesCommand {

  private static final String NAME = "s.go.entity.control";

  protected EntityGOControlCommand() {
    super(NAME);
  }

  public static String generate(UUID uuid) {
    return String.format("/%s %s", NAME, uuid);
  }
}