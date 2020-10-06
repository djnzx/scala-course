package hackerrank.d200320_05.fairrations;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * https://www.hackerrank.com/challenges/fair-rations/problem
 */
public class FairRationsV2 {

  static boolean isInvalid(int[] bread) {
    return (Arrays.stream(bread).sum() & 1) == 1;
  }
  
  static boolean isDone(int[] amount) {
    return Arrays.stream(amount).allMatch(n -> (n & 1) == 0);
  }

  static int findTheOdd(int[] bread) {
    return IntStream.range(0, bread.length)
        .filter(i -> (bread[i] & 1) == 1)
        .findFirst()
        .orElseThrow(RuntimeException::new);
  }

  static Optional<Integer> fairRations(int[] bread) {
    if (isInvalid(bread)) return Optional.empty();
    int steps = 0;
    while (!isDone(bread)) {
      int idx = findTheOdd(bread);
      bread[idx]++;
      bread[idx+1]++;
      steps++;
    }
    return Optional.of(steps*2);
  }

  public static void main(String[] args) {
    Optional.of(1).map(String::valueOf).orElse("NO");
    int[] d = {2, 3, 4, 5, 6};
    System.out.println(fairRations(d));
  }

}
