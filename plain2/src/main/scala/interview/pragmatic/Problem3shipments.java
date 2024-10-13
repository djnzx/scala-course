package interview.pragmatic;

import java.util.Arrays;

public class Problem3shipments {

  static int ships(int n, int[] w) {
    int sum = Arrays.stream(w).sum();
    int avg = sum / n;
    return sum % n !=0 ? -1 : Arrays.stream(w).map(x -> avg-x).filter(x -> x > 0).sum();
  }

  public static void main(String[] args) {
    int[] a = {10,20,2,3,15};
//    int[] a = {1,1,1,1,16};
    int ships = ships(a.length, a);
    System.out.println(ships);
  }
}
