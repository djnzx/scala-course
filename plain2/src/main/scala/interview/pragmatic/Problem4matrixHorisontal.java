package interview.pragmatic;

import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Problem4matrixHorisontal {

  static String traverseHor(int R, int C, int[][] m) {
    StringJoiner sj = new StringJoiner(" ");
    int dir = 1;
    int r = 0;
    int c = 0;
    int cnt = 0;
    int exp = C*R;
    while (cnt < exp) {
      sj.add(String.valueOf(m[r][c]));
      cnt++;
      switch (dir) {
        case 1: if (c < C-1) c++; else { r++; dir++; } break;
        case 2: c--; dir++;                            break;
        case 3: if (c > 0  ) c--; else { r++; dir++; } break;
        case 4: c++; dir=1;                            break;
      }
    }
    return sj.toString();
  }

  static String traverseHor2(int R, int C, int[][] m) {
    return IntStream.range(0, R * C).map(abs -> {
      int row = abs / C;
      int shift = abs - row * C;
      int col = (row&1)==0 ? shift : C-1-shift;
      return m[row][col];
    })
        .mapToObj(String::valueOf)
        .collect(Collectors.joining(" "));
  }

  static List<Integer> traverseHorRXR(int R, int C, int[][] m, int idx, List<Integer> acc) {
    if (idx == R*C) return acc;
    int row = idx / C;
    int shift = idx - row * C;
    int col = (row&1)==0 ? shift : C-1-shift;
    acc.add(m[row][col]);
    return traverseHorRXR(R, C, m, idx+1, acc);
  }

  static String traverseHorRX(int R, int C, int[][] m) {
    return traverseHorRXR(R, C, m, 0, new LinkedList<>()).stream()
        .map(String::valueOf)
        .collect(Collectors.joining(" "));
  }

  static void traverseHorRR1(int R, int C, int[][] m, int r, int c, int dir, int cnt, int exp, StringJoiner sj) {
    if (cnt == exp) return;
    if      (dir==1) if (c < C-1) traverseHorRR1(R, C, m, r, c+1, dir, cnt+1, exp, sj);
                     else         traverseHorRR1(R, C, m, r+1, c, dir+1, cnt+1, exp, sj);
    else if (dir==2)              traverseHorRR1(R, C, m, r, c-1, dir+1, cnt+1, exp, sj);
    else if (dir==3) if (c > 0)   traverseHorRR1(R, C, m, r, c-1, dir, cnt+1, exp, sj);
                     else         traverseHorRR1(R, C, m, r+1, c, dir+1, cnt+1, exp, sj);
    else if (dir==4)              traverseHorRR1(R, C, m, r, c+1, 1, cnt+1, exp, sj);
  }

  static void traverseHorRR(int R, int C, int[][] m, int r, int c, int dir, int cnt, int exp, StringJoiner sj) {
    if (cnt == exp) return;
    sj.add(String.valueOf(m[r][c]));
    int nr = (dir==1 && c==C-1 || dir==3 && c==0) ? r+1 : r;
    int nc = (dir==1 && c<C-1  || dir==4) ? c+1 : (dir==2 || dir==3 && c>0) ? c-1 : c;
    int ndir = (dir==1 && c == C-1 || dir==2 || dir==3 && c==0) ? dir+1 : (dir==4) ? 1 : dir;
    traverseHorRR(R, C, m, nr, nc, ndir, cnt+1, exp, sj);
  }

  static String traverseHorR(int R, int C, int[][] m) {
    StringJoiner sj = new StringJoiner(" ");
    traverseHorRR(R, C, m, 0, 0, 1, 0, C*R, sj);
    return sj.toString();
  }

  public static void main(String[] args) {
    int[][]a = {
        {1,  2, 3},
        {5,  6, 7},
        {9, 10,11},
        {13,14,15},
        {17,18,19},
        {21,22,23},
//        {25,26,27},
//        {1,  2, 3, 4, },
//        {5,  6, 7, 8, },
//        {9, 10,11, 12,},
//        {13,14,15, 16,},
//        {17,18,19, 20,},
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
    };
    System.out.println(traverseHor(a.length, a[0].length, a));
    System.out.println(traverseHor2(a.length, a[0].length, a));
    System.out.println(traverseHorR(a.length, a[0].length, a));
    System.out.println(traverseHorRX(a.length, a[0].length, a));
  }
}
