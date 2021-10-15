package pw.tales.cofdsystem.mod;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class Utils {

  public static <K, V> CompletableFuture<ImmutablePair<K, V>> merge(
      CompletableFuture<K> k,
      CompletableFuture<V> v
  ) {
    return k.thenCompose(kValue -> v.thenApply(vValue -> new ImmutablePair<>(kValue, vValue)));
  }

  public static <T> CompletableFuture<T> ignoreExc(
      CompletableFuture<T> future,
      Class... clazzes
  ) {
    return future.exceptionally(e -> {
      e = getFutureException(e);

      for (Class clazz : clazzes) {
        if (clazz.isAssignableFrom(e.getClass())) {
          return null;
        }
      }

      throw new CompletionException(e);
    });
  }

  public static Throwable getFutureException(Throwable exception) {
    while (exception instanceof CompletionException) {
      exception = exception.getCause();
    }
    return exception;
  }
}
