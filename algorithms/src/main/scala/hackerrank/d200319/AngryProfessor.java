package hackerrank.d200319;

/**
 * https://www.hackerrank.com/challenges/angry-professor/problem
 */

import java.util.Arrays;

public class AngryProfessor {
  static String angryProfessor(int k, int[] a) {
    return Arrays.stream(a).filter(t -> t <= 0).count() >= k ? "YES" : "NO";
  }

}
