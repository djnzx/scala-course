package ninetynine;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class P49J5 {
  public static Stream<String> gray(int n, Stream<String> st) {
    for (st = Stream.of(""); n > 0; n--, st = st.flatMap(s -> Stream.of(0, 1).map(d -> d + s)));
    return st;
  }
  
  public static void main(String[] args) {
    List<String> outcome = gray(3, null).collect(Collectors.toList());
    System.out.println(outcome);
  }
}
