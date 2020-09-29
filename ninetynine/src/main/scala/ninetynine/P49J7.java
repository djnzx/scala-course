package ninetynine;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class P49J7 {
  
  public static Stream<String> gray(int n) {
    return n == 0 ? Stream.of("") : gray(n-1).flatMap(s -> Stream.of(0, 1).map(d -> s +d ));
  }
  
  public static void main(String[] args) {
    List<String> outcome = gray(3).collect(Collectors.toList());
    System.out.println(outcome);
  }
}
