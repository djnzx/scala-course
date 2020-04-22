package hackerrank.d200330_06;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemsDay5 {

  public static void xtralongfact(int n) {
    BigDecimal r = new BigDecimal(1);
    for (int i = 1; i <= n; i++) {
      r = r.multiply(new BigDecimal(i));
    }
    System.out.println(r.toString());
  }

  static String encryption(String sx) {
    final String s2 = sx.replace(" ", "");
    int len = s2.length();
    double l = Math.sqrt(len);
    final int l1 = (int) Math.floor(l);
    final int l2 = (int) Math.ceil(l);
    int cols = l2;
    int rows = l1 == l2 ? l2 : l1*l2 < len ? l2 : l1;

    return IntStream.range(0, cols).boxed().flatMap(c ->
        IntStream.range(0, rows).boxed().map(r -> {
          int idx = r*cols+c;
          String ch = idx < len ? String.valueOf(s2.charAt(idx)) : "";
          return r==rows-1 ? ch +" ": ch;
        })).collect(Collectors.joining());
  }

  public static void main_encryption(String[] args) {
//    System.out.println(encryption("have a nice day"));
//    System.out.println(encryption("feed the dog"));
    System.out.println(encryption("chillout"));
  }

  static int[][] convert(String[] grid) {
    int[][] data = new int[grid.length][grid[0].length()];
    IntStream.range(0, grid.length).forEach(y ->
        IntStream.range(0, grid[0].length()).forEach(x ->
            data[y][x] = grid[y].charAt(x) == 'G' ? 0 : 9));
    return data;
  }

  static boolean freeHorAt(int x, int y, int len, int[][] data) {
    for (int i = 0; i < len; i++) {
      if (data[y][x+i] != 0) return false;
    }
    return true;
  }

  static void occupyHorAt(int x, int y, int len, int[][] data) {
    for (int i = 0; i < len; i++) {
      data[y][x+i] = 1;
    }
  }

  static void releaseHorAt(int x, int y, int len, int[][] data) {
    for (int i = 0; i < len; i++) {
      data[y][x+i] = 0;
    }
  }

  static boolean freeVerAt(int x, int y, int len, int[][] data) {
    for (int i = 0; i < len; i++) {
      if (data[y+i][x] != 0) return false;
    }
    return true;
  }

  static void occupyVerAt(int x, int y, int len, int[][] data) {
    for (int i = 0; i < len; i++) {
      data[y+i][x] = 1;
    }
  }

  static void releaseVerAt(int x, int y, int len, int[][] data) {
    for (int i = 0; i < len; i++) {
      data[y+i][x] = 0;
    }
  }

  static boolean isPossibleAt(int x, int y, int size, int[][] data) {
    return freeHorAt(x, y+size/2, size, data)
        && freeVerAt(x+size/2, y, size, data);
  }

  static void occupyAt(int x, int y, int size, int[][] data) {
    occupyHorAt(x, y+size/2, size, data);
    occupyVerAt(x+size/2, y, size, data);
  }

  static void releaseAt(int x, int y, int size, int[][] data) {
    releaseHorAt(x, y+size/2, size, data);
    releaseVerAt(x+size/2, y, size, data);
  }

  static class PlusAt {
    final int x1;
    final int y1;
    final int x2;
    final int y2;
    final int size1;
    final int size2;

    PlusAt(int x1, int y1, int x2, int y3, int size1, int size2) {
      this.x1 = x1;
      this.y1 = y1;
      this.x2 = x2;
      this.y2 = y3;
      this.size1 = size1;
      this.size2 = size2;
    }
  }

  static class PlusResult {
    public final int size1;
    public final int size2;
    public final boolean ok;

    PlusResult(int size1, int size2, boolean ok) {
      this.size1 = size1;
      this.size2 = size2;
      this.ok = ok;
    }
  }

  // https://www.hackerrank.com/challenges/two-pluses/problem
  static int twoPluses(String[] grid) {
    int[][] data = convert(grid);
    int width = data[0].length;
    int height = data.length;
    int max_w = (width & 1) == 0 ? width - 1 : width;
    int max_h = (height & 1) == 0 ? height - 1 : height;
    int max_size = Math.min(max_w, max_h);
    List<PlusAt> combinations = IntStream.rangeClosed(1, max_size).filter(x -> (x & 1) != 0).map(x -> max_size + 1 - x).boxed().flatMap(size1 ->
        IntStream.rangeClosed(1, max_size).filter(x -> (x & 1) != 0).map(x -> max_size + 1 - x).boxed().flatMap(size2 ->
            IntStream.rangeClosed(0, width - size1).boxed().flatMap(x1 ->
                IntStream.rangeClosed(0, height - size1).boxed().flatMap(y1 ->
                    IntStream.rangeClosed(0, width - size2).boxed().flatMap(x2 ->
                        IntStream.rangeClosed(0, height - size2).boxed().map(y2 ->
                            new PlusAt(x1, y1, x2, y2, size1, size2)))))))
        .collect(Collectors.toList());
    return combinations.stream().map(c -> {
      if (isPossibleAt(c.x1,c.y1,c.size1, data)) {
        occupyAt(c.x1,c.y1,c.size1, data);
        if (isPossibleAt(c.x2,c.y2,c.size2, data)) {
          releaseAt(c.x1,c.y1,c.size1, data);
          return new PlusResult(c.size1, c.size2, true);
        }
        releaseAt(c.x1,c.y1,c.size1, data);
      }
      return new PlusResult(c.size1, c.size2, false);
    })
        .filter(r -> r.ok)
        .mapToInt(r -> (r.size1*2-1)*(r.size2*2-1))
        .max()
        .orElseThrow(RuntimeException::new);
  }

  public static void main_pluses(String[] args) {
    String[] p1 = { // 25
        "BGBBGB",
        "GGGGGG",
        "BGBBGB",
        "GGGGGG",
        "BGBBGB",
        "BGBBGB"};
    String[] p2 = { //5
        "GGGGGG",
        "GBBBGB",
        "GGGGGG",
        "GGBBGB",
        "GGGGGG"};
    System.out.println(twoPluses(p1));
  }

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
