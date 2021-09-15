package glovo.q.b_java;

public class Countries {

  int flood(int y, int x, int[][] data, int color) {
    // out of the board
    if (x < 0 || y < 0 || x >= data[0].length || y >= data.length) return 0;
    // already visited
    if (data[y][x] == 0) return 0;
    // different country
    if (data[y][x] != color) return 0;
    // mark
    data[y][x] = 0;

    int size = 1
      + flood(y-1, x, data, color)
      + flood(y+1, x, data, color)
      + flood(y, x-1, data, color)
      + flood(y, x+1, data, color);

    return size;
  }

  public int count(int[][] data) {
    int counter = 0;
    for (int y = 0; y < data.length; y++) {
      for (int x = 0; x < data[0].length; x++) {
        counter += flood(y, x, data, data[y][x]) > 0 ? 1 : 0;
      }
    }
    return counter;
  }

}
