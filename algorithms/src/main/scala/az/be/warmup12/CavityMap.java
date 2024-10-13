package lesson54s4.warmup;

/**
 * https://www.hackerrank.com/challenges/cavity-map/problem
 */
public class CavityMap {

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

  static String[] cavityMap(String[] grid) {
    String[] answer = new String[grid.length];
    for (int y = 0; y < grid.length; y++) {
      StringBuilder sb = new StringBuilder();
      for (int x = 0; x < grid[y].length(); x++) {
        char ch = isCavityAt(x, y, grid) ? 'X' : (char) at(x, y, grid);
        sb.append(ch);
      }
      answer[y] = sb.toString();
    }
    return answer;
  }

}
