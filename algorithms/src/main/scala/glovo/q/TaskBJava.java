package glovo.q;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TaskBJava {

  static class Point {
    final int x;
    final int y;

    private Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public static Point of(int x, int y) {
      return new Point(x, y);
    }

    public Point move(Point p) {
      return Point.of(
        this.x + p.x,
        this.y + p.y
      );
    }

    @Override
    public String toString() {
      return String.format("%d:%d", x, y);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Point point = (Point) o;
      return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y);
    }
  }

  static final Set<Point> DELTAS = new HashSet<Point>() {{
    add(Point.of(0, -1));
    add(Point.of(-1, 0));
    add(Point.of(+1, 0));
    add(Point.of(0, +1));
  }};

  static final int VISITED = Integer.MIN_VALUE;

  int[][] board;
  int HEIGHT;
  int WIDTH;

  int counter = 0;

  void markVisited(Point p) {
    board[p.y][p.x] = VISITED;
  }
  
  void markVisited(Collection<Point> pts) {
    pts.forEach(this::markVisited);
  }

  boolean isOnBoard(Point p) {
    return p.x >= 0 && p.x < WIDTH && p.y >= 0 && p.y < HEIGHT;
  }
  
  boolean isUnvisited(Point p) {
    return board[p.y][p.x] != VISITED;
  }
  
  boolean isCountry(Point p, int country) {
    return board[p.y][p.x] == country;
  }

  // neighbours on the board
  Stream<Point> neighbours(Point point) {
    return DELTAS.stream()
      .map(d -> d.move(point))
      .filter(this::isOnBoard);
  }

  // filter unvisited and my
  Set<Point> neighboursUnvisitedMy(Set<Point> points, int country) {
    return points.stream().flatMap(this::neighbours)
      .filter(this::isUnvisited)
      .filter(p -> isCountry(p, country))
      .collect(Collectors.toSet());
  }

  void flood(Point p) {
    int country = board[p.y][p.x];
    Set<Point> step = new HashSet<>();
    step.add(p);
    while (!step.isEmpty()) {
      markVisited(step);
      step = neighboursUnvisitedMy(step, country);
    }
  }

  /**
   * this implementation 
   * DOES MUTATE the original array !
   */
  public int solution(int[][] a) {
    board = a;
    HEIGHT = a.length;
    WIDTH = a[0].length;
    
    IntStream.range(0, HEIGHT).boxed().flatMap(y ->
      IntStream.range(0, WIDTH).boxed().map(x ->
        Point.of(x, y)
      )
    )
      .filter(this::isUnvisited)
      .forEach(p -> { flood(p); counter++; } );

    return counter;
  }

  void pa() {
    String s = Arrays.stream(board)
      .map(x -> Arrays.stream(x).boxed().map(i -> String.format("%d", i)).collect(Collectors.joining(" ")))
      .collect(Collectors.joining("\n"));

    System.out.println(s);
    System.out.println(counter);
    System.out.println("-------------");
  }

}
