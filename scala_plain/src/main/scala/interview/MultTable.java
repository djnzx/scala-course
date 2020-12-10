package interview;

import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MultTable {

  public static void main(String[] args) {
    Supplier<Stream<Integer>> range = () -> IntStream.rangeClosed(1, 10).boxed();

    String collected = range.get().flatMap(first ->
        range.get().map(second -> String.format("%d * %d = %d", first, second, first * second)))
        .collect(Collectors.joining("\n"));
    System.out.println(collected);
  }

}
