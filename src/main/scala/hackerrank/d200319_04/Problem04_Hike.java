package hackerrank.d200319_04;

/**
 * https://www.hackerrank.com/challenges/counting-valleys/problem
 */
public class Problem04_Hike {
  static int count(int n, String s) {
    int level = 0;
    int count = 0;
    int prev = 0;
    for (int i = 0; i < n; i++) {
      char c = s.charAt(i);
      switch (c) {
        case 'D': prev = level; level--; break;
        case 'U': prev = level; level++; break;
      }
      if (level==0 && prev<0) count++;
    }
    return count;
  }

  public static void main(String[] args) {
    System.out.println(count(8, "UDDDUDUU"));
  }
}
