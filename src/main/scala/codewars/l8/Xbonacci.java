package codewars.l8;

import java.util.Arrays;

public class Xbonacci {

  public double[] tribonacci(double[] s, int n) {
    double[] r = new double[n];
    System.arraycopy(s, 0, r, 0, Math.min(n, 3));
    for (int i=3; i<n; i++) {
      r[i] = r[i-1] + r[i-2] + r[i-3];
    }
    return r;
  }

  public static void main(String[] args) {
    Xbonacci variabonacci = new Xbonacci();
    double precision = 1e-10;

    System.out.println(Arrays.toString(variabonacci.tribonacci(new double []{1,1,1},10)));
  }
}
