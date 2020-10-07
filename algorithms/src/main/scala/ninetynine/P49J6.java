package ninetynine;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class P49J6 {
  
  public static Stream<String> gray(int n, Stream<String> st) {
    return n == 0 ? st : gray(n-1, st.flatMap(s -> Stream.of(0, 1).map(d -> s + d)));
  }
  
  public static void main(String[] args) {
    List<String> outcome = gray(3, Stream.of("")).collect(Collectors.toList());
    System.out.println(outcome);
  }
}
