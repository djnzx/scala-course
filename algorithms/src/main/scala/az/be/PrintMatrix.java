package az.be;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PrintMatrix {

  public static String dataOrdered(int R, int C, int[][] m) {
    return IntStream.range(0, R*C).map(idx -> {
      int row = idx / C;
      int shift = idx - row * C;
      int col = (row&1)==0 ? shift : C-1-shift;
      return m[row][col];
    })
        .mapToObj(String::valueOf)
        .collect(Collectors.joining(" "));
  }

  public static List<Integer> dataOrdered2R(int R, int C, int[][] m, int idx, List<Integer> acc) {
    if (idx == R*C) return acc;
    int row = idx / C;
    int shift = idx - row * C;
    int col = (row&1)==0 ? shift : C-1-shift;
    acc.add(m[row][col]);
    return dataOrdered2R(R, C, m, idx+1, acc);
  }

  public static String dataOrdered2(int R, int C, int[][] m) {
    return dataOrdered2R(R, C, m, 0, new LinkedList<>()).stream()
        .map(String::valueOf)
        .collect(Collectors.joining(" "));
  }

  public static void main(String[] args) {
    int[][]a =
        {
        {  1,  2,  3 },
        {  5,  6,  7 },
        {  9, 10, 11 },
        { 13, 14, 15 },
        { 17, 18, 19 },
        { 21, 22, 23 },
    };
    System.out.println(dataOrdered(a.length, a[0].length, a));
    System.out.println(dataOrdered2(a.length, a[0].length, a));
    // 1 2 3 7 6 5 9 10 11 15 14 13 17 18 19 23 22 21
  }
}
