package interview.pragmatic;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Problem5contest {

  static int[] primes(int MAX) {
    return IntStream.rangeClosed(2, MAX).filter(n ->
        IntStream.range(2, (int)(Math.sqrt(n)+1))
            .allMatch(x -> n % x != 0)
    ).toArray();
  }

  static int popularity(int len, int[] pop) {
    int[] primes = primes(1000);
    return IntStream.range(0, len).flatMap(idx1 ->
        Arrays.stream(primes)
            .map(p -> idx1 + p)
            .filter(idx2 -> idx2 < len)
            .map(idx2 -> pop[idx2]-pop[idx1])
    ).sum();
  }

  public static void main(String[] args) {
    int[] test = {2,4,6,8,10,12,14};
    int pop = popularity(test.length, test);
    System.out.println(pop);
  }

}
