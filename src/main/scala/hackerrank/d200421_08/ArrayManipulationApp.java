package hackerrank.d200421_08;

/**
 * https://www.hackerrank.com/challenges/crush/problem
 */
public class ArrayManipulationApp {

  static long arrayManipulation(int n, int[][] queries) {
    int max = 0;
    int []process = new int[n+1];
    for (int[] query : queries) {
      process[query[0] - 1] += query[2];
      process[query[1]] -= query[2];
    }

    int temp = 0;
    for (int value : process) {
      temp += value;
      if (temp > max) max = temp;
    }
    return  max;
  }

}
