package graphs.lee;

import java.util.Optional;
import java.util.Set;

public class LauncherJava {

  public static void main(String[] args) {
    LeeMutableJava lee = new LeeMutableJava(20, 15);
    Pt src = Pt.of(0, 0);
    Pt dst =
//      Point.of(19, 14);
      Pt.of(19, 0);
    Set<Pt> obstacles = Set.of(
      Pt.of(5,14),
      Pt.of(5,13),
      Pt.of(5,12),
      Pt.of(5,11),
      Pt.of(5,10),
      Pt.of(5,9),
      Pt.of(5,8),

      Pt.of(10,0),
      Pt.of(10,1),
      Pt.of(10,2),
      Pt.of(10,3),
      Pt.of(10,4),
      Pt.of(10,5),
      Pt.of(10,6),
      Pt.of(10,7),
      Pt.of(10,8)
    );

    System.out.println(1);
    Optional<Iterable<Pt>> trace = lee.trace(src, dst, obstacles);
    System.out.println(2);
    System.out.println(trace);
    System.out.println();
    System.out.println(lee);
    System.out.println();
    trace.ifPresent(path -> System.out.println(lee.boardFormatted(path)));
  }

}
