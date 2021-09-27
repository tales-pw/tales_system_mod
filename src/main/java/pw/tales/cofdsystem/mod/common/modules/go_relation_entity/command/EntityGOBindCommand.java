package pw.tales.cofdsystem.mod.common.modules.go_relation_entity.command;

import java.util.Collections;
import java.util.List;
import pw.tales.cofdsystem.mod.common.TalesCommand;


public abstract class EntityGOBindCommand extends TalesCommand {

  private static final String NAME = "s.go.entity.bind";
  private static final String SHORT_NAME = "s.go.bind";

  protected EntityGOBindCommand() {
    super(NAME);
  }

  public static String generate(String dn) {
    return String.format("/%s %s", NAME, dn);
  }

  @Override
  public List<String> getAliases() {
    return Collections.singletonList(SHORT_NAME);
  }
}