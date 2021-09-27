package pw.tales.cofdsystem.mod.common.haxe_adapters;

import haxe.lang.Function;
import java.util.function.Consumer;

public class HaxeFn<T> extends Function {

  private final Consumer<T> consumer;

  private HaxeFn(Consumer<T> consumer) {
    super(1, 1);
    this.consumer = consumer;
  }

  public static <F> HaxeFn<F> wrap(Consumer<F> consumer) {
    return new HaxeFn<>(consumer);
  }

  @Override
  public double __hx_invoke1_f(double v, Object o) {
    if (o != null) {
      this.consumer.accept((T) o);
    }
    return 0;
  }
}
