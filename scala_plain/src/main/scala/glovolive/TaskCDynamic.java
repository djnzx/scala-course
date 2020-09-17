package glovolive;

import java.util.HashSet;
import java.util.Set;

public class TaskCDynamic {

  public static int hasSubArrayWithSum(int[] xs, int k) {
    int count = 0;
    Set<Integer> prefix = new HashSet<>();
    int sum = 0;

    for (int x: xs) {
      sum += x;
      if (sum == k) count++;
      if (prefix.contains(sum - k)) count++;
      prefix.add(sum);
    }

    return count;
  }
  
  /**
   * non empty subarray with sum = K
   * prefix sum
   */
  public static boolean containsZeroSubArray(int[] xs) {
    return hasSubArrayWithSum(xs, 0) > 0;
  }

}
