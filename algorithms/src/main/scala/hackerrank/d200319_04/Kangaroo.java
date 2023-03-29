package hackerrank.d200319_04;

public class Kangaroo {
  /**
   * https://www.hackerrank.com/challenges/kangaroo/problem
   */
  static String represent(boolean r) {
    return r ? "YES" : " NO";
  }

  static boolean canMeet(int x1, int v1, int x2, int v2) {
    double v = (double) (x2 - x1) / (v1 - v2);
    return (int) v == v;
  }

  static boolean isTrivial(int x1, int v1, int x2, int v2) {
    return x1 == x2 && v1 == v2;
  }

  static boolean canBeSolved(int x1, int v1, int x2, int v2) {
    return (x2 > x1 && v1 > v2) || (x2 < x1 && v1 < v2);
  }

  static String kangaroo(int x1, int v1, int x2, int v2) {
    boolean r = isTrivial(x1, v1, x2, v2)
        || (canBeSolved(x1, v1, x2, v2) && canMeet(x1, v1, x2, v2));
    return represent(r);
  }

}
