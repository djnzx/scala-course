package graphs.leejava;

public class Point {
  public final int x;
  public final int y;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Point point = (Point) o;
    return x == point.x && y == point.y;
  }

  @Override
  public int hashCode() {
    return x << 16 + y;
  }

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public static Point of(int x, int y) {
    return new Point(x, y);
  }

  @Override
  public String toString() {
    return String.format("[%d,%d]", x, y);
  }

  public Point move(int dx, int dy) {
    return new Point(x + dx, y + dy);
  }
}