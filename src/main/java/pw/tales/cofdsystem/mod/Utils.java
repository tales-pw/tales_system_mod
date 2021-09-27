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
      while (e instanceof CompletionException) {
        e = e.getCause();
      }

      for (Class clazz : clazzes) {
        if (clazz.isAssignableFrom(e.getClass())) {
          return null;
        }
      }

      throw new CompletionException(e);
    });
  }
}
