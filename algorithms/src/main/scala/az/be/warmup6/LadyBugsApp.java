package lesson44s02.warmup;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LadyBugsApp {
  private final static String YES = "YES";
  private final static String NO = "NO";

  private static boolean hasSingles(String bag) {
    return bag.codePoints()
        .filter(c -> c != '_').boxed()
        .collect(Collectors.groupingBy(c -> c, Collectors.counting()))
        .containsValue(1L);
  }

  private static String squeeze(final String bag) {
    return IntStream.range(0, bag.length()).boxed()
        .flatMap(idx -> {
          char curr =                  bag.charAt(idx);
          char prev = idx == 0 ? '_' : bag.charAt(idx - 1);
          return curr != prev ? Stream.of(String.valueOf(curr)) : Stream.empty();
        })
        .collect(Collectors.joining());
  }

  /**
   * AAABBBCCCDDEEEE
   * A  B  C  D E     => len=5, distinct = 5 => ordered
   *
   * AAABBBCCCDDEEEEA
   * A  B  C  D E   A => len6, distinct = 5 => NOT ordered
   */
  private static boolean alreadyOrdered(String bag) {
    String sq = squeeze(bag);
    return sq.length() == sq.codePoints().distinct().count();
  }

  private static String happyLadybugs(String bag) {
    return
        hasSingles(bag) ? NO :
        bag.contains("_") ? YES :
        alreadyOrdered(bag) ? YES :
        NO;
  }

  public static void main(String[] args) {
    System.out.println(happyLadybugs("CBBCAA"));
  }

}
