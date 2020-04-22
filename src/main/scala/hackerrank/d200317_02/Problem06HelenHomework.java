package hackerrank.d200317_02;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Problem06HelenHomework {

  static class Pair {
    final int a;
    final int b;

    Pair(int a, int b) {
      this.a = a;
      this.b = b;
    }
  }

  static boolean isPerfect(int[] data) {
    return IntStream.range(0, data.length-1).allMatch(idx -> data[idx] < data[idx + 1]);
  }

  static int indexOfMinFrom(int[] data, int from) {
    return IntStream.range(from, data.length).mapToObj(idx -> new Pair(idx, data[idx]))
        .min((p1,p2) -> p1.b - p2.b)
        .map(p -> p.a)
        .orElse(0);
  }

  static int lilysHomework(int[] arr) {
    int counter = 0;
    int currentPos = 0;
    while (currentPos < arr.length && !isPerfect(arr)) {
      int minIdx = indexOfMinFrom(arr, currentPos);
      if (minIdx != currentPos) {
        // swap
        int t = arr[currentPos];
        arr[currentPos] = arr[minIdx];
        arr[minIdx] = t;
        counter++;
        System.out.printf("%s : %d\n",Arrays.toString(arr),counter);
      }
      currentPos++;
    }
    return counter;
  }

  public static void main(String[] args) {
    int[] d = {3, 4, 2, 5, 1};
    System.out.printf("%s : 0\n",Arrays.toString(d));
    int r = lilysHomework(d);
    System.out.println(r);
  }
}
