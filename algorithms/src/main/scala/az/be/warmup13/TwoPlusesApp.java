package lesson53s3.warmup;

import java.util.stream.IntStream;

/**
 * https://www.hackerrank.com/challenges/two-pluses/problem
 */
public class TwoPlusesApp {

  private final static int FREE = 0;
  private final static int OCCUPIED = 1;
  private final static int BUSY = 9;

  static int[][] convert(String[] grid) {
    int dimY = grid.length;
    int dimX = grid[0].length();
    int[][] data = new int[dimY][dimX];

    IntStream.range(0, dimY).forEach(y ->
        IntStream.range(0, dimX).forEach(x ->
            data[y][x] = grid[y].charAt(x) == 'G' ? FREE : BUSY
        ));
    return data;
  }

  static boolean isFreeHorAt(int x, int y, int len, int[][] data) {
    return IntStream.range(0, len).allMatch(i -> data[y][x+i] == FREE);
  }

  static boolean isFreeVerAt(int x, int y, int len, int[][] data) {
    return IntStream.range(0, len).allMatch(i -> data[y+i][x] == FREE);
  }

  static boolean isPossibleAt(int x, int y, int size, int[][] data) {
    return isFreeHorAt(x, y + size/2, size, data)
        && isFreeVerAt(x + size/2, y, size, data);
  }

  static void occupyHorAt(int x, int y, int len, int[][] data) {
    IntStream.range(0, len).forEach(i -> data[y][x+i] = OCCUPIED);
  }

  static void occupyVerAt(int x, int y, int len, int[][] data) {
    IntStream.range(0, len).forEach(i -> data[y+i][x] = OCCUPIED);
  }

  static void occupyAt(int x, int y, int size, int[][] data) {
    // size/2 - center of the plus
    occupyHorAt(x, y + size/2, size, data);
    occupyVerAt(x + size/2, y, size, data);
  }

  static void releaseHorAt(int x, int y, int len, int[][] data) {
    IntStream.range(0, len).forEach(i -> data[y][x+i] = FREE);
  }

  static void releaseVerAt(int x, int y, int len, int[][] data) {
    IntStream.range(0, len).forEach(i -> data[y+i][x] = FREE);
  }

  static void releaseAt(int x, int y, int size, int[][] data) {
    // size/2 - center of the plus
    releaseHorAt(x, y + size/2, size, data);
    releaseVerAt(x + size/2, y, size, data);
  }

  static class PlusesAt {
    final int x1;
    final int y1;
    final int x2;
    final int y2;
    final int size1;
    final int size2;

    PlusesAt(int x1, int y1, int x2, int y2, int size1, int size2) {
      this.x1 = x1;
      this.y1 = y1;
      this.x2 = x2;
      this.y2 = y2;
      this.size1 = size1;
      this.size2 = size2;
    }
  }

  static class PlusesRes {
    public final int size1;
    public final int size2;
    public final boolean fit;

    PlusesRes(int size1, int size2, boolean fit) {
      this.size1 = size1;
      this.size2 = size2;
      this.fit = fit;
    }

    public static PlusesRes yes(int size1, int size2) {
      return new PlusesRes(size1, size2, true);
    }

    public static PlusesRes no(int size1, int size2) {
      return new PlusesRes(size1, size2, false);
    }
  }

  static PlusesRes tryToFit(PlusesAt p, int[][] data) {
    if (isPossibleAt(p.x1, p.y1, p.size1, data)) {
      occupyAt(p.x1, p.y1, p.size1, data);
      if (isPossibleAt(p.x2, p.y2, p.size2, data)) {
        releaseAt(p.x1, p.y1, p.size1, data);
        return PlusesRes.yes(p.size1, p.size2);
      }
      releaseAt(p.x1, p.y1, p.size1, data);
    }
    return PlusesRes.no(p.size1, p.size2);
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
    int max_size = Math.min(max_h, max_w);

    return
    IntStream.rangeClosed(1, max_size)       // all sizes for pluses
        .filter(s -> (s & 1) != 0)            // filter only Odd ones
        .map(x -> max_size + 1 - x)
        .boxed().flatMap(size1 ->

            IntStream.rangeClosed(1, max_size) // all sizes for pluses
                .filter(s -> (s & 1) != 0)      // filter only Odd ones
                .map(x -> max_size + 1 - x)
                .boxed().flatMap(size2 ->

                IntStream.rangeClosed(0, w - size1).boxed().flatMap(x1 ->
                    IntStream.rangeClosed(0, h - size1).boxed().flatMap(y1 ->
                        IntStream.rangeClosed(0, w - size2).boxed().flatMap(x2 ->
                            IntStream.rangeClosed(0, h - size2).boxed().map(y2 ->
                                new PlusesAt(x1, y1, x2, y2, size1, size2)
                            )
                        )
                    )
                )
            )
        )
    .map(ps -> tryToFit(ps, data))
    .filter(pr -> pr.fit)
    .mapToInt(pr -> area(pr))
    .max()
    .orElseThrow(RuntimeException::new);
  }

}
