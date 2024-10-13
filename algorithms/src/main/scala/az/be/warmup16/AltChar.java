package lesson49.warmup;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class AltChar {

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

  static List<Character> sToList(String s) {
    return s.chars().mapToObj(c -> (char)c).collect(Collectors.toList());
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

  static int alternatingCharacters(String s) {
    BiFunction<
        Pair<Integer, Character>,
        Character,
        Pair<Integer, Character>
    > f = (p, c) -> p.b != c ? new Pair<>(p.a, c) : new Pair<>(p.a+1, c);

    return fold(
        sToList(s),
        new Pair<>(0, '_'),
        f
    ).a;
  }

  static int alternatingCharacters2(String s) {
    return fold(
        sToList(s),
        new Pair<>(0, '_'),
        (p, c) -> p.b != c ? new Pair<>(p.a, c) : new Pair<>(p.a+1, c)
    ).a;
  }

  static int length(String s) {
    BiFunction<
        Integer,
        Character,
        Integer
    > f = (i, c) -> i+1;

    return fold(
        sToList(s),
        0,
        f
    );
  }

  public static void main(String[] args) {
    String s = "ABBCCDD";
    System.out.println(s);
    System.out.println(alternatingCharacters(s));
  }
}
