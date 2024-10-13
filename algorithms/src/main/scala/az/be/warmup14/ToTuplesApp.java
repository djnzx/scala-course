package lesson51s1.warmup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ToTuplesApp {

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

  static class Item extends Pair<Character, Long> {
    Item(Character ch, Long cnt) {
      super(ch, cnt);
    }
  }

  static class Acc extends Pair<List<Item>, Optional<Item>> {
    Acc(List<Item> items, Optional<Item> item) {
      super(items, item);
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

  static List<Character> sToList(String s) {
    return s.chars().mapToObj(c -> (char)c).collect(Collectors.toList());
  }

  /**
   * implement in terms of .fold
   */
  static List<Item>toTuples(String origin) {
    Acc initial = new Acc(new ArrayList<>(), Optional.empty());
    Acc folded = fold(
        sToList(origin),
        initial,
        (acc, ch) -> {
          List<Item> result = acc.a;
          Optional<Item> buf = acc.b;
          // if buffer is empty
          if (!buf.isPresent()) return new Acc(acc.a, Optional.of(new Item(ch, 1L)));
          // if char in the buffer is the same with given char
          if (buf.get().a.equals(ch)) return new Acc(result, Optional.of(new Item(ch, buf.get().b+1)));
          // if char in the buffer is different
          buf.ifPresent(b -> result.add(b));
          return new Acc(result, Optional.of(new Item(ch, 1L)));
        }
    );
    folded.b.ifPresent(buf -> folded.a.add(buf));
    return folded.a;
  }

  public static void main(String[] args) {
    System.out.println(toTuples("aabbcbbbccdddd"));
  }
}
