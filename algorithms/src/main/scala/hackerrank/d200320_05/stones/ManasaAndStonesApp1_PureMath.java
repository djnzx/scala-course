package hackerrank.d200320_05.stones;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ManasaAndStonesApp1_PureMath {

  static int[] stones(int n, int a, int b) {
    return IntStream.range(0, n)
        .map(x -> x * a + (n - 1 - x) * b)
        .distinct()
        .sorted()
        .toArray();
  }

  public static void main(String[] args) {
    System.out.println(Arrays.toString(stones(4, 10, 100)));
  }
}
