package az.be;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PrintDiagonal {
  static class Track {
    int r = 0;
    int c = 0;
    int dir = 0;
  }

  private static String traverse(int R, int C, int[][] a) {
    Track t = new Track();
    return IntStream.range(0, R*C).map(n -> {
      int val = a[t.r][t.c];
      switch (t.dir) {
        case 0: t.c++; t.dir=1; break;
        case 1:
          if      (t.r == R-1) { t.c++; t.dir=2; }
          else if (t.c == 0  ) { t.r++; t.dir=2; }
          else                 { t.r++; t.c--;   }
          break;
        case 2:
          if      (t.c == C-1) { t.r++; t.dir=1; }
          else if (t.r == 0)   { t.c++; t.dir=1; }
          else                 { t.r--; t.c++;   }
      }
      return val;
    })
        .mapToObj(String::valueOf)
        .collect(Collectors.joining(" "));
  }

  public static void main(String[] args) {
    int[][]a = {
        {  1,  2,  3,  4, },
        {  5,  6,  7,  8, },
        {  9, 10, 11, 12, },
        { 13, 14, 15, 16, },
        { 17, 18, 19, 20, },
        { 21, 22, 23, 24, },
        { 25, 26, 27, 28, },
    };
    System.out.println("1 2 5 9 6 3 4 7 10 13 17 14 11 8 12 15 18 21 25 22 19 16 20 23 26 27 24 28");
    System.out.println(traverse(a.length, a[0].length, a));
  }

}
