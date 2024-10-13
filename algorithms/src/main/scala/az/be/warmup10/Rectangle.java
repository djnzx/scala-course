package lesson56s6.warmup;

import java.util.stream.IntStream;
import java.util.stream.Stream;

class Rectangle {
  private final Point p1;
  private final Point p2;

  static Rectangle random() {
    return new Rectangle(Point.random(), Point.random());
  }

  Rectangle(Point p1, Point p2) {
    this.p1 = p1;
    this.p2 = p2;
  }

  public int left() {
    return Math.min(p1.x, p2.x);
  }

  public int top() {
    return Math.max(p1.y, p2.y);
  }

  public int right() {
    return Math.max(p1.x, p2.x);
  }

  public int bottom() {
    return Math.min(p1.y, p2.y);
  }

  private Stream<Integer> x_range() {
    return IntStream.range(left(), right()).boxed();
  }

  private Stream<Integer> y_range() {
    return IntStream.range(top(), bottom()).boxed();
  }

  public Stream<Point> points() {
    return x_range().flatMap(x ->
        y_range().map(y ->
            new Point(x, y)));
  }

}
