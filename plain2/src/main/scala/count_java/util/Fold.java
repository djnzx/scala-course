package count_java.util;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class Fold {

  public static <C, A> A fold(Stream<C> data, A zero, BiFunction<A, C, A> f) {
    return fold(data.iterator(), zero, f);
  }

  public static <C, A> A fold(Iterable<C> data, A zero, BiFunction<A, C, A> f) {
    return fold(data.iterator(), zero, f);
  }

  public static <C, A> A fold(Iterator<C> it, A zero, BiFunction<A, C, A> f) {
    A acc = zero;
    while (it.hasNext()) acc = f.apply(acc, it.next());
    return acc;
  }

}
