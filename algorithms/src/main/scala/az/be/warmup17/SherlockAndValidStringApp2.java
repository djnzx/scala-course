package lesson48s06.wamup;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SherlockAndValidStringApp2 {

  static class Pair<A, B> {
    final A a;
    final B b;

    Pair(A a, B b) {
      this.a = a;
      this.b = b;
    }
  }

  private static List<Pair<Long, List<Character>>> prepareData(String s) {
    return s.chars().mapToObj(c -> (char) c)
        .collect(Collectors.groupingBy(c -> c, Collectors.counting()))
        .entrySet().stream()
        .collect(Collectors.groupingBy(
            Map.Entry::getValue,
            Collectors.mapping(
                Map.Entry::getKey,
                Collectors.toList()
            )
        ))
        .entrySet().stream()
        .sorted((o1, o2) -> Long.compare(o1.getKey(), o2.getKey()))
        .map(e -> new Pair<>(e.getKey(), e.getValue()))
        .collect(Collectors.toList());
  }

  private static boolean solve(List<Pair<Long, List<Character>>> m) {
    if (m.size() == 1) return true;
    if (m.size() > 2) return false;
    if (m.get(1).a - m.get(0).a == 1 && m.get(1).b.size() == 1) return true;
    if (m.get(0).a == 1 && m.get(0).b.size() == 1) return true;
    return false;
  }

  private static String represent(boolean solution) {
    return solution ? "YES" : "NO";
  }

  public static String isValid(String s) {
    List<Pair<Long, List<Character>>> m = prepareData(s);
    boolean solved = solve(m);
    return represent(solved);
  }

  public static void main(String[] args) {
    isValid("abcc");
  }
}
