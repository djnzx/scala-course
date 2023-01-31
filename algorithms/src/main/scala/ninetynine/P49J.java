package ninetynine;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class P49J {
  
  public static List<String> gray(int n) {
    return n == 0 ? Collections.singletonList("") :
        Stream.of("0", "1").flatMap(d -> gray(n - 1).stream().map(s -> d + s)).collect(Collectors.toList());
  }

  public static Stream<String> grays(int n) {
    return n == 0 ? Stream.of("") : Stream.of("0", "1").flatMap(d -> grays(n - 1).map(s -> d + s));
  }

  public static void main(String[] args) {
    System.out.println(grays(4).collect(Collectors.toList()));
  }
}
