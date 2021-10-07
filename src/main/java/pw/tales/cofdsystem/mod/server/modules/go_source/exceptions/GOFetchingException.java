package pw.tales.cofdsystem.mod.server.modules.go_source.exceptions;

public class GOFetchingException extends RuntimeException {

  private final String dn;

  public GOFetchingException(String msg, String dn) {
    super(msg);
    this.dn = dn;
  }

  public String getDN() {
    return dn;
  }
}
