package interview.pragmatic;

import java.util.Arrays;

public class Problem2permut {

  public static String times(int n, char c) {
    byte[] bytes = new byte[n];
    Arrays.fill(bytes, (byte) c);
    return new String(bytes);
  }

  public static void perm(int N) {
    int c = (N+1)/2;
    for (int i = 1; i <= c; i++) {
      String s1 = times(i, '*');
      String s2 = times(N-i, '#');
      String s = (i&1)!=0 ? s1+s2 : s2+s1;
      System.out.println(s);
    }
    boolean even = (N&1)==0;
    if (!even) c--;
    for (int i = c; i > 0 ;i--) { // even - ok // odd - need to change order
      String s1 = times(i, '*');
      String s2 = times(N-i, '#');
      String s = ((i&1)==0)^(!even) ? s1+s2 : s2+s1;
//      if (!even) s = new StringBuilder(s).reverse().toString();
      System.out.println(s);
    }
  }

  public static void main(String[] args) {
    perm(7);
  }
}
