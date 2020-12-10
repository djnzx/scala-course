package interview.pragmatic;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Problem4matrixVertical {

  static String traverseVer(int R, int C, int[][] m) {
    return IntStream.range(0, R * C).map(idx -> {
      int col_r = idx / R;
      int shift = idx - col_r * R;
      int row = (col_r&1)==0 ? shift : R-1-shift;
      int col = C-1-col_r;
      return m[row][col];
    })
        .mapToObj(String::valueOf)
        .collect(Collectors.joining(" "));
  }

  public static void main(String[] args) {
    int[][]a = {
        {1,  2, 3, 4, },
        {5,  6, 7, 8, },
        {9, 10,11, 12,},
        {13,14,15, 16,},
        {17,18,19, 20,},
    };
//        {1,  2, 3},
//        {5,  6, 7},
//        {9, 10,11},
//        {13,14,15},
//        {17,18,19},
//        {21,22,23},
//        {25,26,27},
//        {21,22,23, 24,},
//        {25,26,27, 28,},
//        {1,  2,},
//        {5,  6,},
//        {9, 10,},
//        {13,14,},
//        {17,18,},
//        {21,22,},
//        {25,26,},
//        {1,  2, 3, 4,  100},
//        {5,  6, 7, 8,  101},
//        {9, 10,11, 12, 102},
//        {13,14,15, 16, 103},
//        {17,18,19, 20, 104},
//        {21,22,23, 24, 105},
//        {25,26,27, 28, 106},
    // reference output
    System.out.println("4 8 12 16 20 19 15 11 7 3 2 6 10 14 18 17 13 9 5 1");
    // your output
    System.out.println(traverseVer(a.length, a[0].length, a));
  }
}
