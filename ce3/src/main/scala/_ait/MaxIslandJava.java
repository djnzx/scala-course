package _ait;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MaxIslandJava {

  static class Pt {
    final int x;
    final int y;

    Pt(int x, int y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public String toString() {
      return String.format("%d:%d", x, y);
    }

    Pt l() {
      return new Pt(x - 1, y);
    }

    Pt r() {
      return new Pt(x + 1, y);
    }

    Pt u() {
      return new Pt(x, y - 1);
    }

    Pt d() {
      return new Pt(x, y + 1);
    }

    Stream<Pt> neighbours() {
      return Stream.of(l(), r(), u(), d());
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Pt pt = (Pt) o;
      return x == pt.x && y == pt.y;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y);
    }
  }

  static boolean isBetween(int a, int min, int max) {
    return a >= min && a <= max;
  }

  static boolean insideBoard(Pt p, int[][] surface) {
    return isBetween(p.y, 0, surface.length - 1) &&
      isBetween(p.x, 0, surface[0].length - 1);
  }

  static boolean isLand(Pt p, int[][] surface) {
    return surface[p.y][p.x] == 1;
  }

  static Set<Pt> doCount(Pt pt, int[][] surface, Set<Pt> current) {
    if (current.contains(pt)) return new HashSet<>();
    current.add(pt);
    pt.neighbours()
      .filter(p -> insideBoard(p, surface))
      .filter(p -> isLand(p, surface))
      .flatMap(p -> doCount(p, surface, current).stream())
      .forEach(current::add);
    return current;
  }

  public static int maxIslandArea(int[][] surface) {
    return
      IntStream.range(0, surface.length).flatMap(y ->
          IntStream.range(0, surface[0].length)
            .filter(x -> isLand(new Pt(x, y), surface))
            .map(x ->
              doCount(new Pt(x, y), surface, new HashSet<>()).size()
            )
        )
        .reduce(0, Math::max);
  }

  public static void main(String[] args) {
    int[][] surface = {
      {0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0},
      {0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0},
      {0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0},
      {0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0},
      {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0}
    };
    System.out.println(maxIslandArea(surface));
  }

}
