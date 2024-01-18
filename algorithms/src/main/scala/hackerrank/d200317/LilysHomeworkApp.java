package hackerrank.d200317;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * https://www.hackerrank.com/challenges/lilys-homework/problem
 * doesn't work
 */
public class LilysHomeworkApp {

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

  static void swap(int[] arr, int i1, int i2) {
    int t = arr[i1];
    arr[i1] = arr[i2];
    arr[i2] = t;
  }

  static int lilysHomework(int[] arr) {
    int counter = 0;
    int currentPos = 0;
    while (currentPos < arr.length && !isPerfect(arr)) {
      int minIdx = indexOfMinFrom(arr, currentPos);
      if (minIdx != currentPos) {
        swap(arr, currentPos, minIdx);
        counter++;
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
