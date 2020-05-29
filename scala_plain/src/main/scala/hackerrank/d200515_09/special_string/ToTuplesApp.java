package hackerrank.d200515_09.special_string;

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
    Item(Character character, Long aLong) {
      super(character, aLong);
    }
    static Optional<Item> one(char ch) {
      return Optional.of(new Item(ch, 1L));
    }
    Item inc() {
      return new Item(a, b+1);
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
          if (!acc.b.isPresent())       return new Acc(acc.a, Item.one(ch));
          if (acc.b.get().a.equals(ch)) return new Acc(acc.a, acc.b.map(Item::inc));
          acc.a.add(acc.b.get());
                                        return new Acc(acc.a, Item.one(ch));
        }
    );
    folded.b.ifPresent(folded.a::add);
    return folded.a;
  }

  /**
   * [P[a:2], P[b:2], P[c:1], P[b:3], P[c:2], P[d:4]]
   */
  public static void main(String[] args) {
    System.out.println(toTuples("aabbcbbbccdddd"));
  }
}
