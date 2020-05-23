package hackerrank.d200320_05.stones;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ManasaAndStonesApp2_Iterative {

  static List<Integer> next(List<Integer> curr, int a, int b) {
    return curr.stream().flatMap(n -> Stream.of(n+a, n+b)).distinct().collect(Collectors.toList());
  }

  static int[] stones(int n, int a, int b) {
    List<Integer> step = Arrays.asList(a, b);
    while (--n>1) {
      step = next(step, a, b);
    }
    return step.stream().mapToInt(x->x).distinct().sorted().toArray();
  }

  public static void main(String[] args) {
    System.out.println(Arrays.toString(stones(4,10,100)));
  }

}
