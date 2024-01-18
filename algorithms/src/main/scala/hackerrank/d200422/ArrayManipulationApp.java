package hackerrank.d200422;

/**
 * https://www.hackerrank.com/challenges/crush/problem
 */
public class ArrayManipulationApp {

  static long arrayManipulation(int n, int[][] queries) {
    long []process = new long[n+1];
    for (int[] query : queries) {
      int idx_plus = query[0] - 1;
      int idx_minus = query[1];
      int val = query[2];

      process[idx_plus] += val;
      process[idx_minus] -= val;
    }

    long max = 0;
    long temp = 0;
    for (long value: process) {
      temp += value;
      max = Math.max(max, temp);
    }
    return  max;
  }

}
