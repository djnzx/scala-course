package hackerrank.d200330;

public class ProblemsDay5 {

  static void almostSorted(int[] a) {
    boolean prev = true;
    boolean can = true;
    int cnt = 0;
    int len = 0;
    int idx1 = -1;
    int idx2 = -1;

    for (int i = 0; i < a.length - 1; i++) if (can) {

      if (a[i] < a[i+1]) {
        // ok, or F->T switch
      } else { // a[i] > a[i+1], wrong order, or T->F switch
        // first switch
        if (cnt == 0) {
          cnt++;
          idx1 = i;
        }
      }


    }

    // only one switch
    if (cnt == 1) {
      idx2 = a.length-1;
    }
    // pick swap or reverse

    if (!can) {
      System.out.println("no");
    } else {
      System.out.println("yes");
      if (true)
      System.out.printf("swap %d %d\n", idx1+1, idx2+1);
      else
        System.out.printf("rotate %d %d\n", idx1+1, idx2+1);
    }
  }

  public static void mainAlmostSorted(String[] args) {
    int[] a1 = {1,2};
    int[] a2 = {4,2};
    almostSorted(a1);
  }

  public static boolean isSorted(int[] a) {
    for (int i = 0; i < a.length - 1; i++) {
      if (a[i+1] < a[i]) return false;
    }
    return true;
  }

  public static int indexOfUnsorted(int[] a) {
    for (int i = 0; i < a.length - 1; i++) {
      if (a[i+1] - a[i] != 1) return i+1;
    }
    throw new RuntimeException("should be unsorted");
  }

  //https://www.hackerrank.com/challenges/larrys-array/problem
  static String larrysArray(int[] a) {
    if (a.length<3) return "NO";


    throw new IllegalArgumentException();
  }

  public static void main(String[] args) {
    int a1[] = {1,2,3};
    int a2[] = {1,2,3,5,4};
    int a3[] = {1,2,3,4,2};
    System.out.println(isSorted(a1));
    System.out.println(isSorted(a2));
    System.out.println(indexOfUnsorted(a2));
    System.out.println(indexOfUnsorted(a3));
  }

}
