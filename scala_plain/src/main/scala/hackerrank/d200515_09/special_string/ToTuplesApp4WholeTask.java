package hackerrank.d200515_09.special_string;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ToTuplesApp4WholeTask {

  static long oneToN(long n) {
    return n * (n+1) / 2;
  }

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

  static class Tri<A, B, C> {
    public final A a;
    public final B b;
    public final C c;

    Tri(A a, B b, C c) {
      this.a = a;
      this.b = b;
      this.c = c;
    }

    static Tri<Item, Item, Item> from(int i, List<Item> a) {
      return new Tri<>(a.get(i), a.get(i+1), a.get(i+2));
    }

    @Override
    public String toString() {
      return String.format("T[%s:%s:%s]", a, b, c);
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

  static <A> List<A> addToList(List<A> list, Optional<A> item) {
    item.ifPresent(list::add);
    return list;
  }

  static class Acc extends Pair<List<Item>, Optional<Item>> {
    Acc(List<Item> items, Optional<Item> item) {
      super(items, item);
    }
    Acc withItem(Optional<Item> itm) {
      return new Acc(a, itm);
    }
    Acc inc() {
      return new Acc(a, b.map(Item::inc));
    }
    Acc process(char ch) {
      // empty
      if (!b.isPresent())       return withItem(Item.one(ch));
      // same
      return b.get().a.equals(ch) ? inc() : new Acc(addToList(a, b), Item.one(ch));
    }
    List<Item> attachLast() {
      return addToList(a, b);
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
    return fold(
        sToList(origin),
        new Acc(new ArrayList<>(), Optional.empty()),
        Acc::process
    )
        .attachLast();
  }

  static long count(String origin) {
    List<Item> items = toTuples(origin);
    System.out.println(items);
    long sum1 = items.stream().mapToLong(itm -> oneToN(itm.b)).sum();

    long sum2 =
        IntStream.range(0, items.size() - 2)
        .mapToObj(i -> Tri.from(i, items))
        .filter(t -> t.b.b == 1) // center
        .filter(t -> t.a.a == t.c.a) // same chars
        .mapToLong(t -> oneToN(Math.min(t.a.b, t.c.b)))
        .sum();
    return sum1+sum2;
  }
  /**
   * [P[a:2], P[b:2], P[c:1], P[b:3], P[c:2], P[d:4]]
   */
  public static void main(String[] args) {
    String s = "aabbcbbbccdddd";
    System.out.println(toTuples(s));
    count(s);
  }
}
