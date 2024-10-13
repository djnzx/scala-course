package lesson49.warmup;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class MinMax {

  static class Pair<A, B> {
    final A a;
    final B b;

    Pair(A a, B b) {
      this.a = a;
      this.b = b;
    }

    @Override
    public String toString() {
      return String.format("P[%s:%s]", a, b);
    }
  }

  static <T, A> A fold(Iterable<T> data, A initial, BiFunction<A, T, A> f) {
    Iterator<T> it = data.iterator();
    A acc = initial;
    while (it.hasNext()) {
      System.out.println(acc);
      acc = f.apply(acc, it.next());
    }
    return acc;
  }

  static Pair<Integer, Integer> minmax(List<Integer> data) {
    return fold(data,
        new Pair<>(Integer.MAX_VALUE, Integer.MIN_VALUE),
        (mm, item) ->
            new Pair<>(
                Math.min(item, mm.a),
                Math.max(item, mm.b)
            )
    );
  }

  public static void main(String[] args) {
    List<Integer> data = Arrays.asList(1, 6, 3, 8, 3, 2, 9, 0);
    System.out.println(minmax(data));
  }
}
