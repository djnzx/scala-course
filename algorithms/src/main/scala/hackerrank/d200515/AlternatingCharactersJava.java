package hackerrank.d200515;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class AlternatingCharactersJava {

  static class Pair<A, B> {
    final A a;
    final B b;

    Pair(A a, B b) {
      this.a = a;
      this.b = b;
    }
  }

  static <A, B> B foldLeft(Collection<A> data, B empty, BiFunction<B, A, B> f) {
    Iterator<A> it = data.iterator();
    B acc = empty;
    while (it.hasNext()) {
      acc = f.apply(acc, it.next());
    }
    return acc;
  }

  static List<Character> sToList(String s) {
    return s.chars().mapToObj(c -> (char)c).collect(Collectors.toList());
  }

  static int alternatingCharacters(String s) {
    return foldLeft(
        sToList(s),
        new Pair<>(0, '_'),
        (Pair<Integer, Character> ap, Character c) -> ap.b != c ? new Pair<>(ap.a, c) : new Pair<>(ap.a+1, c)
    ).a;
  }

}
