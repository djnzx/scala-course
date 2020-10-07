package ninetynine;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class P49J4 {
  public static Stream<String> gray(int n) {
    Stream<String> st = Stream.of("");
    while (n > 0) {
      st = st.flatMap(s -> Stream.of("0", "1").map(d -> d + s));
      n--;
    }
    return st;
  }
  
  public static void main(String[] args) {
    List<String> outcome = gray(3).collect(Collectors.toList());
    System.out.println(outcome);
  }
}
