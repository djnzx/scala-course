package hackerrank.d200421_08;

/**
 * https://www.hackerrank.com/challenges/crush/problem
 */
public class ArrayManipulationApp {

  static long arrayManipulation(int n, int[][] queries) {
    long []process = new long[n+1];
    for (int[] query : queries) {
      process[query[0] - 1] += query[2];
      process[query[1]    ] -= query[2];
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
