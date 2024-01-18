package hackerrank.d200320.cavitymap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * https://www.hackerrank.com/challenges/cavity-map/problem
 */
public class CavityMapV1 {
  
  static int[][] convert(String[] strings) {
    int len = strings.length;
    int[][] data = new int[len][len];
    IntStream.range(0, len).forEach(y ->
        IntStream.range(0, len).forEach(x ->
            data[y][x] = (byte) (strings[y].charAt(x) - '0')
        ));
    return data;
  }

  static char toChar(int x) {
    return x < 0 ? 'X' : (char) (x + '0');
  }

  static String bytesToString(int[] bytes) {
    byte[] bb = new byte[bytes.length];
    for (int i = 0; i < bytes.length; i++) bb[i] = (byte) toChar(bytes[i]);
    return new String(bb);
  }

  static String bytesToStringF(int[] bytes) {
    return Arrays.stream(bytes)
        .mapToObj(CavityMapV1::toChar)
        .map(String::valueOf)
        .collect(Collectors.joining());
  }

  static String[] represent(int[][] a) {
    return Arrays.stream(a)
        .map(CavityMapV1::bytesToString)
        .toArray(String[]::new);
  }

  static boolean isCavityAt(int[][] data, int x, int y) {
    int c = data[y+1][x+1];
    return c > data[y  ][x+1] 
        && c > data[y+1][x  ] 
        && c > data[y+1][x+2]
        && c > data[y+2][x+1];
  }

  static boolean isBorder(int x, int y, int size) {
    return x==0 || x==size-1 || y==0 || y==size-1;
  }

  static boolean isNoNeighbours(int x, int y, boolean[][] c) {
    return c[y][x] && !c[y-1][x] && !c[y][x-1];
  }
  
  private static int[][] combine(int[][] a, boolean[][] c) {
    int size = a.length;
    int[][] combined = new int[size][size];
    IntStream.range(0, size).forEach(y ->
        IntStream.range(0, size).forEach(x -> 
          combined[y][x] = !isBorder(x,y,size) && isNoNeighbours(x, y, c) ? -1 : a[y][x]
        )
    );
    return combined;
  }

  static String[] cavityMap(String[] grid) {
    int size = grid.length;
    if (size < 3) return grid;
    int[][] a = convert(grid);
    boolean[][] cavity = new boolean[size][size];
    IntStream.rangeClosed(0, a.length - 3).forEach(dx ->
        IntStream.rangeClosed(0, a.length - 3).forEach(dy ->
            cavity[dy+1][dx+1]=isCavityAt(a, dx, dy)
        ));
    int[][] outcome = combine(a, cavity);
    return represent(outcome);
  }

  public static String[] read() throws IOException {
    String fname = "scala_plain/src/main/resources/100.txt";
    return Files.readAllLines(Paths.get(fname)).toArray(new String[0]);
  }
  
  public static void writeAnswer(String[] outcome) throws IOException {
    String fname = "scala_plain/src/main/resources/100answerMY.txt";
    Files.write(Paths.get(fname), Arrays.asList(outcome));
  }
  
  public static void main(String[] args) throws IOException {
    String[] grid = read();
    String[] r = cavityMap(grid);
    writeAnswer(r);
  }

}
