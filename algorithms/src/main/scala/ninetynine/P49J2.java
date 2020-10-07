package ninetynine;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class P49J2 {

  public static Stream<String> gray(int n) {
    if (n == 0) return Stream.of("");

    List<String> prev = gray(n - 1).collect(Collectors.toList());

    return Stream.of("0", "1").flatMap(d -> prev
      .stream()
      .map(s -> d + s)
    );
  }
  
  public static void main(String[] args) {
    List<String> outcome = gray(2).collect(Collectors.toList());
    System.out.println(outcome);
  }
}
