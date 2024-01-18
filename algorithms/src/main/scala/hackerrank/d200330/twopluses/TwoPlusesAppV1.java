package hackerrank.d200330.twopluses;

import java.util.stream.IntStream;

/**
 * https://www.hackerrank.com/challenges/two-pluses/problem
 */
public class TwoPlusesAppV1 {
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

  static class PlusesAt {
    final int x1;
    final int y1;
    final int x2;
    final int y2;
    final int size1;
    final int size2;

    PlusesAt(int x1, int y1, int x2, int y3, int size1, int size2) {
      this.x1 = x1;
      this.y1 = y1;
      this.x2 = x2;
      this.y2 = y3;
      this.size1 = size1;
      this.size2 = size2;
    }
  }

  static class PlusesRes {
    public final int size1;
    public final int size2;
    public final boolean ok;

    PlusesRes(int size1, int size2, boolean ok) {
      this.size1 = size1;
      this.size2 = size2;
      this.ok = ok;
    }
  }

  static PlusesRes tryToFit(PlusesAt p, int[][] data) {
    if (isPossibleAt(p.x1,p.y1,p.size1, data)) {
      occupyAt(p.x1,p.y1,p.size1, data);
      if (isPossibleAt(p.x2,p.y2,p.size2, data)) {
        releaseAt(p.x1,p.y1,p.size1, data);
        return new PlusesRes(p.size1, p.size2, true);
      }
      releaseAt(p.x1,p.y1,p.size1, data);
    }
    return new PlusesRes(p.size1, p.size2, false);
  }
  
  static int area(PlusesRes pr) {
    int s1 = pr.size1 * 2 - 1;
    int s2 = pr.size2 * 2 - 1;
    return s1 * s2;
  }

  static int twoPluses(String[] grid) {
    int[][] data = convert(grid);
    int w = data[0].length;
    int h = data.length;
    int max_w = (w & 1) == 0 ? w - 1 : w;
    int max_h = (h & 1) == 0 ? h - 1 : h;
    int max_size = Math.min(max_w, max_h);
    return
        IntStream.rangeClosed(1, max_size)
            .filter(x -> (x & 1) != 0)
            .map(x -> max_size + 1 - x).boxed().flatMap(size1 ->
            
            IntStream.rangeClosed(1, max_size)
                .filter(x -> (x & 1) != 0)
                .map(x -> max_size + 1 - x).boxed().flatMap(size2 ->
                
                IntStream.rangeClosed(0, w - size1).boxed().flatMap(x1 ->
                    IntStream.rangeClosed(0, h - size1).boxed().flatMap(y1 ->
                        IntStream.rangeClosed(0, w - size2).boxed().flatMap(x2 ->
                            IntStream.rangeClosed(0, h - size2).boxed().map(y2 ->
                                
                                new PlusesAt(x1, y1, x2, y2, size1, size2)))))))

            .map(ps -> tryToFit(ps, data))
            .filter(r -> r.ok)
            .mapToInt(r -> area(r))
            .max()
            .orElseThrow(RuntimeException::new);
  }
  
  public static void main(String[] args) {
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
}
