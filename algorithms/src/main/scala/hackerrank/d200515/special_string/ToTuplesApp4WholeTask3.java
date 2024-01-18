package hackerrank.d200515.special_string;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

/**
 * still doesn't execute within the time limits
 */
public class ToTuplesApp4WholeTask3 {

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

  static class Item extends Pair<Character, Integer> {
    Item(Character character, Integer aLong) {
      super(character, aLong);
    }
    Item inc() {
      return new Item(a, b+1);
    }
  }

  static <A> List<A> addToList(List<A> list, A item) {
    list.add(item);
    return list;
  }

  static List<Character> one(char ch) {
    return new LinkedList<Character>() {{ add(ch); }};
  }

  static Item listToItem(List<Character> list) {
    return new Item(list.get(0), list.size());
  }

  static class Acc extends Pair<List<Item>, List<Character>> {

    Acc(List<Item> items, List<Character> item) {
      super(items, item);
    }

    Acc inc() {
      return new Acc(a, addToList(b, b.get(0)));
    }

    Acc process(char ch) {
      if (b.isEmpty()) return new Acc(a, one(ch));
      return b.get(0).equals(ch) ? inc() : new Acc(addToList(a, listToItem(b)), one(ch));
    }

    List<Item> attachLast() {
      return addToList(a, listToItem(b));
    }
  }

  static <T, A> A fold(Iterable<T> data, A initial, BiFunction<A, T, A> f) {
    Iterator<T> it = data.iterator();
    A acc = initial;
    while (it.hasNext()) {
      acc = f.apply(acc, it.next());
    }
    return acc;
  }

  static Iterable<Character> sToIterable(String s) {
    return () -> new Iterator<Character>() {
      int pos = 0;
      public boolean hasNext() {
        return pos < s.length();
      }
      public Character next() {
        return s.charAt(pos++);
      }
    };
  }

  static List<Item>toTuples(String origin) {
    return fold(
        sToIterable(origin),
        new Acc(new LinkedList<>(), new LinkedList<>()),
        Acc::process
    )
        .attachLast();
  }

  static long count(String origin) {
    List<Item> items = toTuples(origin);
    AtomicLong counter = new AtomicLong(0);
    items.forEach(itm -> counter.addAndGet(oneToN(itm.b)));
    IntStream.range(0, items.size() - 2).forEach(i -> {
          Item l = items.get(i);
          Item r = items.get(i+2);
          if (items.get(i+1).b == 1 && l.a == r.a) counter.addAndGet(oneToN(Math.min(l.b, r.b)));
        });
    return counter.get();
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
