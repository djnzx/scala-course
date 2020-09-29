package ninetynine;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class P49J3 {
  private static final Supplier<Stream<String>> variants = () -> Stream.of("0", "1");

  public static Stream<String> gray() {
    return Stream.of("")
      .flatMap(s -> variants.get().map(d -> d + s))
      .flatMap(s -> variants.get().map(d -> d + s))
      .flatMap(s -> variants.get().map(d -> d + s));
  }
  
  public static void main(String[] args) {
    List<String> outcome = gray().collect(Collectors.toList());
    System.out.println(outcome);
  }
}
