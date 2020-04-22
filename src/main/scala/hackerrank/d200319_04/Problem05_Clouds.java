package hackerrank.d200319_04;

public class Problem05_Clouds {
  static int calc(int[] clouds) {
    final int SAFE = 0;
    int len = clouds.length;
    int count = 0;
    int curr = 0;
    while (curr < len - 1) {
      if (curr+2<len && clouds[curr+2] == SAFE) {
        count++;
        curr+=2;
        continue;
      }
      if (curr+1<len && clouds[curr+1] == SAFE) {
        count++;
        curr+=1;
        continue;
      }
      throw new RuntimeException("no way");
    }
    return count;
  }

  public static void main(String[] args) {
    int c = calc(new int[]{0, 0, 1, 0, 0, 1, 0});
    System.out.println(c);
  }
}
