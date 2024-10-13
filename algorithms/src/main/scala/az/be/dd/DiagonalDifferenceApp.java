package az.be.dd;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class DiagonalDifferenceApp {

  public static int diagonalDifference(List<List<Integer>> data) {
    class Pair {
      final int a;
      final int b;

      Pair(int a, int b) {
        this.a = a;
        this.b = b;
      }
    }
    return IntStream.range(0, data.size()).mapToObj(i -> new Pair(
        data.get(i).get(i),
        data.get(i).get(data.size()-1-i)
    ))
        .reduce((p1, p2) -> new Pair(p1.a+p2.a, p1.b+p2.b))
        .map(p -> Math.abs(p.a - p.b))
        .orElseThrow(RuntimeException::new);
  }

  public static int diagonalDifference2(List<List<Integer>> data) {
    int r = IntStream.range(0, data.size()).map(i ->
        data.get(i).get(i) -
            data.get(i).get(data.size()-1-i)
    ).sum();
    return Math.abs(r);
  }

  public static void main(String[] args) {
    List<List<Integer>> data = Arrays.asList(
        Arrays.asList(1, 2, 3),
        Arrays.asList(3, 4, 5),
        Arrays.asList(5, 6, 8)
    );
    int r = diagonalDifference(data);
    System.out.println(r);
  }
}
