package az.be.kangaroo;

// https://www.hackerrank.com/challenges/kangaroo/problem
public class KangarooApp {

  static String represent(boolean r) {
    return r ? "YES" : " NO";
  }

  private static boolean solve(int x1, int v1, int x2, int v2) {
    double v = (double) (x2-x1) / (v1-v2);
    return (int) v == v;
  }

  private static boolean canBeSolved(int x1, int v1, int x2, int v2) {
    return (x2>x1 && v1>v2) || (x2<x1 && v1<v2) || (x1==x2 && v1==v2);
  }

  static String kangaroo(int x1, int v1, int x2, int v2) {
    boolean r = canBeSolved(x1, v1, x2, v2) && solve(x1, v1, x2, v2);
    return represent(r);
  }

}
