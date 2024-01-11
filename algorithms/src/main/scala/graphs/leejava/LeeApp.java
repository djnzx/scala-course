package graphs.leejava;

import java.util.Optional;
import java.util.Set;

public class LeeApp {

  public static void main(String[] args) {
    Lee lee = new Lee(20, 15);
    Point src = Point.of(0, 0);
    Point dst =
//      Point.of(19, 14);
      Point.of(19, 0);
    Set<Point> obstacles = Set.of(
      Point.of(5,14),
      Point.of(5,13),
      Point.of(5,12),
      Point.of(5,11),
      Point.of(5,10),
      Point.of(5,9),
      Point.of(5,8),

      Point.of(10,0),
      Point.of(10,1),
      Point.of(10,2),
      Point.of(10,3),
      Point.of(10,4),
      Point.of(10,5),
      Point.of(10,6),
      Point.of(10,7),
      Point.of(10,8)
    );

    System.out.println(1);
    Optional<Iterable<Point>> trace = lee.trace(src, dst, obstacles);
    System.out.println(2);
    System.out.println(trace);
    System.out.println();
    System.out.println(lee);
    System.out.println();
    trace.ifPresent(path -> System.out.println(lee.boardFormatted(path)));
  }

}
