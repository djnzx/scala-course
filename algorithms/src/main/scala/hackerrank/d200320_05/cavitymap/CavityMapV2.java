package hackerrank.d200320_05.cavitymap;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * https://www.hackerrank.com/challenges/cavity-map/problem
 */
public class CavityMapV2 {

  static int at(int x, int y, String[] grid) {
    return grid[y].charAt(x);
  }

  static boolean isCavityAt(int x, int y, String[] grid) {
    return x > 0 && y > 0
        && y < grid.length-1
        && x < grid[y].length()-1
        && at(x, y, grid) > at(x-1, y, grid)
        && at(x, y, grid) > at(x, y-1, grid)
        && at(x, y, grid) > at(x, y+1, grid)
        && at(x, y, grid) > at(x+1, y, grid);
  }

  static char solve(int x, int y, String[] grid) {
    return isCavityAt(x, y, grid) ? 'X' : (char) at(x, y, grid);
  }

  static String[] cavityMap(String[] grid) {
    return IntStream.range(0, grid.length).mapToObj(y ->
        IntStream.range(0, grid[y].length())
            .mapToObj(x -> solve(x, y, grid))
            .map(String::valueOf)
            .collect(Collectors.joining())
    ).toArray(String[]::new);
  }

}
