package lesson48s06.wamup;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SherlockAndValidStringApp {

  private final static String YES = "YES";
  private final static String NO = "NO";

  static class Pair<A, B> {
    final A a;
    final B b;

    Pair(A a, B b) {
      this.a = a;
      this.b = b;
    }
  }

  public static String isValid(String s) {
    Map<Character, Long> freq = s.chars().mapToObj(c -> (char) c)
        .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

    Map<Long, List<Character>> freqInv = freq.entrySet().stream()
        .collect(Collectors.groupingBy(
            e -> e.getValue(),
            Collectors.mapping(
                e -> e.getKey(),
                Collectors.toList())
            )
        );

    List<Pair<Long, List<Character>>> m = freqInv.entrySet().stream()
        .sorted(Comparator.comparingLong(Map.Entry::getKey))
        .map(e -> new Pair<>(e.getKey(), e.getValue()))
        .collect(Collectors.toList());

    if (m.size() == 1) return YES;
    if (m.size() > 2) return NO;
    // I know that that I have only 2 letters
    if (m.get(1).a - m.get(0).a == 1 && m.get(1).b.size() == 1) return YES;
    if (m.get(0).a==1 && m.get(0).b.size()==1) return YES;
    return NO;
  }

  public static void main(String[] args) {
    isValid("abcc");
  }
}
