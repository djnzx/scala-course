package hackerrank.d200320_05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Problems200320 {

  static class Pair<A, B> {
    final A a;
    final B b;

    Pair(A a, B b) {
      this.a = a;
      this.b = b;
    }
  }

  static byte[][] stoa(String[] strings) {
    int len = strings.length;
    byte[][] data = new byte[len][len];
    for (int i = 0; i < len; i++) {
      for (int j = 0; j < len; j++) {
        data[i][j] = (byte) (strings[i].charAt(j) - '0');
      }
    }
    return data;
  }

  static String[] represent(byte[][] a) {
    String[] res = new String[a.length];
    byte[] bytes = new byte[a.length];
    for (int i = 0; i < a.length; i++) {
      for (int j = 0; j < a.length; j++)
        bytes[j] = (byte) (a[i][j] < 0 ? 'X' : a[i][j] +'0');
      res[i] = new String(bytes);
    }
    return res;
  }

  static boolean isCavityAt(byte[][] data, int x, int y) {
    int c = data[y+1][x+1];
    return c > data[y][x+1] &&
        c > data[y+1][x] && c > data[y+1][x+2]
        && c > data[y+2][x+1];
  }

  static String[] cavityMap(String[] grid) {
    if (grid.length < 3) return grid;
    byte[][] a = stoa(grid);
    IntStream.rangeClosed(0, a.length-3).boxed().flatMap(dx ->
        IntStream.rangeClosed(0, a.length-3).mapToObj(dy ->
            new Pair<>(new Pair<>(dx, dy), isCavityAt(a, dx, dy))
        ))
        .filter(p -> p.b)
        .map(p -> p.a)
        .forEach(p -> a[p.b+1][p.a+1] = -1);
    return represent(a);
  }


  public static void main1(String[] args) throws IOException {
    String[] grid = Files.lines(Paths.get("100.txt")).toArray(String[]::new);
    String[] r = cavityMap(grid);
    for (String s: r) {
      System.out.println(s);
    }
    Files.write(Paths.get("my.txt"), Arrays.asList(r));
  }

  // all even
  static boolean isDone(int[] amount) {
    return Arrays.stream(amount).allMatch(n -> (n & 1) == 0);
  }

  // only one odd
  static boolean isWrongState(int[] amount) {
    return Arrays.stream(amount).filter(n -> (n & 1) == 1).count() == 1;
  }

  // find the 1st
  static int findTheOdd(int[] bread) {
    for (int i = 0; i < bread.length; i++) {
      if ((bread[i]&1) == 1) return i;
    }
    throw new IllegalArgumentException("must be!");
  }

  static Optional<Integer> fairRations(int[] bread) {
    int step=0;
    while (!isDone(bread) && !isWrongState(bread)) {
      int idx = findTheOdd(bread);
      bread[idx]++;
      bread[idx+1]++;
      step++;
    }
    if (isDone(bread)) return Optional.of(step*2);
    return Optional.empty();
  }

  public static void main(String[] args) {
    Optional.of(1).map(n -> Integer.toString(n)).orElse("NO");
    int[] d = {2, 3, 4, 5, 6};
    System.out.println(fairRations(d));
  }

}
