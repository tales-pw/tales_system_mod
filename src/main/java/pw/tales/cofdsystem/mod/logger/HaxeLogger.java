package pw.tales.cofdsystem.mod.logger;

import javax.annotation.Nullable;
import org.apache.logging.log4j.Logger;
import pw.tales.cofdsystem.utils.logger.ABCLogger;

public class HaxeLogger extends ABCLogger {

  private final Logger logger;

  public HaxeLogger(Logger logger) {
    this.logger = logger;
  }

  @Override
  public void info(@Nullable String text) {
    logger.info(text);
  }

  @Override
  public void warning(@Nullable String text) {
    logger.warn(text);
  }

  @Override
  public void error(@Nullable String text) {
    logger.error(text);
  }
}
