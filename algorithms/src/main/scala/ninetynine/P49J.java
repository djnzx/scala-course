package ninetynine;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class P49J {
  
  private static final Supplier<Stream<String>> variants = () -> Stream.of("0", "1");
  
  public static List<String> gray(int n) {
    if (n == 0) return Stream.of("").collect(Collectors.toList());

    List<String> prev = gray(n - 1);

    return variants.get()
      .flatMap(d ->
        prev.stream().map(s -> d + s)
      ).collect(Collectors.toList());
  }
  
  public static void main(String[] args) {
    System.out.println(gray(4));
  }
}
