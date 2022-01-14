package pw.tales.cofdsystem.mod.server.clients.accounts.exceptions;

import java.io.IOException;

public class UnknownResponse extends IOException {

  public UnknownResponse(int code, String responseData) {
    super(String.format(
        "Error response (%d): %s",
        code,
        responseData
    ));
  }
}
