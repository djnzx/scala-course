package hackerrank.d200316_01;

import java.util.stream.IntStream;

public class Problem01_HourGlasses {

  static int sum(int[][] a) {
    int size = a.length;
    int s = 0;
    for (int x = 0; x < size; x++) {
      s += a[0][x];
      s += a[size-1][x];
    }
    s += a[1][1];
    return s;
  }

  static int[][] pickAt(int[][] src, int row, int col) {
    int[][] part = new int[3][3];
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        part[y][x] = src[y + row][x + col];
      }
    }
    return part;
  }

  static int sumAt(int[][] src, int row, int col) {
    int size = 3;
    int sum = src[row + 1][col + 1];
    for (int x = 0; x < size; x++) {
      sum += src[row]    [col + x];
      sum += src[row + 2][col + x];
    }
    return sum;
  }

  static int hourglassSum2(int[][] arr) {
    int rep = arr.length - 2;
    return IntStream.range(0, rep).boxed().flatMap(y ->
        IntStream.range(0, rep).boxed().map(x ->
            sumAt(arr, y, x)
        ))
        .max((o1, o2) -> o1 - o2)
        .orElseThrow(RuntimeException::new);
  }

  static int hourglassSum1(int[][] arr) {
    int rep = arr.length - 2;
    return IntStream.range(0, rep).boxed().flatMap(y ->
        IntStream.range(0, rep).boxed().map(x ->
            pickAt(arr, y, x)
        ))
        .map(Problem01_HourGlasses::sum)
        .max((o1, o2) -> o1 - o2)
        .orElseThrow(RuntimeException::new);
  }

  public static void main(String[] args) {
    int r = 0;
    System.out.printf("The answer is: %d\n", r);
  }
}
