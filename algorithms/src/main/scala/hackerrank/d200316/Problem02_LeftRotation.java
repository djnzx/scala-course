package hackerrank.d200316;

import java.util.Arrays;

public class Problem02_LeftRotation {

  static int[] rotLeft(int[] a, int d) {
    int[] b = new int[a.length];
    System.arraycopy(a, 0,     b, a.length - d, d);
    System.arraycopy(a, d, b, 0, a.length - d );
    return b;
  }

  public static void main(String[] args) {
    int src[] = {1,2,3,4,5};
    int dst[] = rotLeft(src,5);
    System.out.println(Arrays.toString(src));
    System.out.println(Arrays.toString(dst));
  }
}
