package hackerrank.d200320_05.stones;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

public class ManasaAndStonesApp3_Recursive2 {

  private final static Stream<Integer> STEP1 = Stream.of(0);

  static Stream<Integer> step(int n, Function<Integer, Stream<Integer>> m, Stream<Integer> acc) {
    return n == 0 ? acc : step(n-1, m, acc.flatMap(m).distinct());
  }

  static int[] stones(int n, int a, int b) {
    Function<Integer, Stream<Integer>> mapper = x -> Stream.of(x+a, x+b);
    return step(n-1, mapper, STEP1)
        .sorted()
        .mapToInt(z -> z)
        .toArray();
  }

  public static void main(String[] args) {
    System.out.println(Arrays.toString(stones(4, 10, 100)));
  }
}
