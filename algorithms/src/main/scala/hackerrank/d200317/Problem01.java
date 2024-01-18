package hackerrank.d200317;

public class Problem01 {
  static void countSwaps(int[] a) {
    int count=0;
    for (int i = 0; i < a.length; i++) {
      for (int j = 0; j < a.length - 1; j++) {
        // Swap adjacent elements if they are in decreasing order
        if (a[j] > a[j + 1]) {
          int t = a[j];
          a[j] = a[j + 1];
          a[j + 1] = t;
          count++;
        }
      }
    }
    System.out.printf("Array is sorted in %d swaps.\n", count);
    System.out.printf("First Element: %d\n", a[0]);
    System.out.printf("Last Element: %d\n", a[a.length-1]);
  }
  public static void main(String[] args) {
    int r = 0;
    System.out.printf("The answer is: %d\n", r);
  }
}
