package lesson56s6.warmup;

final class Point {
  public final int x;
  public final int y;

  static Point random() {
    return new Point(
        Fn.random_val(C.POINT_MIN_X, C.POINT_MAX_X),
        Fn.random_val(C.POINT_MIN_Y, C.POINT_MAX_Y)
    );
  }

  Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (o == null) return false;
    if (!(o instanceof Point)) return false;

    Point that = (Point) o;

    return this.x == that.x
        && this.y == that.y;
  }

  @Override
  public int hashCode() {
    return x << 16 + y;
  }
}
