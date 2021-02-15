package dominik;

import java.util.Arrays;

public class CountCountries {
  public int[][] matrixA;
  public int[][] matrixB;

  public CountCountries(int[][] matrixA) {
    this.matrixA = matrixA;
    this.matrixB = Arrays.stream(matrixA).map(int[]::clone).toArray(int[][]::new);
  }

  public int countCountries() {
    if (this.matrixA.length == 0 || this.matrixA[0].length == 0)
      return 0;
    
    int count = 0;
    int n = this.matrixA.length;
    int m = this.matrixA[0].length;

    for (int row = 0; row < n; row++) {
      for (int col = 0; col < m; col++) {
        if (this.matrixB[row][col] >= 0) {
          countryColours(row, col, n, m);
          count++;
        }
      }
    }
    return count;
  }

  private void countryColours(int row, int col, int n, int m) {
    if (this.matrixB[row][col] == -1) return;

    this.matrixB[row][col] = -1;

    if (row + 1 < n) {
      if (this.matrixA[row + 1][col] == this.matrixA[row][col]) {
        countryColours(row + 1, col, n, m);
      }
    }
    if (row - 1 >= 0) {
      if (this.matrixA[row - 1][col] == this.matrixA[row][col]) {
        countryColours(row - 1, col, n, m);
      }
    }
    if (col + 1 < m) {
      if (this.matrixA[row][col + 1] == this.matrixA[row][col]) {
        countryColours(row, col + 1, n, m);
      }
    }
    if (col - 1 >= 0) {
      if (this.matrixA[row][col - 1] == this.matrixA[row][col]) {
        countryColours(row, col - 1, n, m);
      }
    }
  }
}
