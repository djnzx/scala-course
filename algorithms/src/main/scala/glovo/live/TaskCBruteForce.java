package glovo.live;

import java.util.stream.IntStream;

public class TaskCBruteForce {

  private static int subSum(int[]a, int i1, int i2) {
    return IntStream.rangeClosed(i1, i2).map(i -> a[i]).sum();
  }

  /**
   * non empty subarray with sum = 0
   */
  public static boolean containsZeroSubArray(int[] a) {
    if (a == null || a.length == 0) return false; 
    
    return IntStream.range(0, a.length).flatMap(i1 ->
      IntStream.range(i1, a.length).map(i2 ->
        subSum(a, i1, i2)
      )
    ).anyMatch(sum -> sum ==0);
  }

}
