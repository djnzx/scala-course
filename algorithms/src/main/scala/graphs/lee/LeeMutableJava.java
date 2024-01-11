package graphs.lee;

import graphs.lee.colored.Ansi;
import graphs.lee.colored.Attribute;
import graphs.lee.colored.Colored;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class LeeMutableJava {
  private static final int EMPTY = 0;
  private static final int START = 1;
  private static final int OBSTACLE = -10;
  private final int width;
  private final int height;
  private final int[][] board;

  public LeeMutableJava(int width, int height) {
    this.width = width;
    this.height = height;
    this.board = new int[height][width];
  }

  private int get(int x, int y) {
    return board[y][x];
  }

  private void set(int x, int y, int value) {
    board[y][x] = value;
  }

  private int get(Pt p) {
    return get(p.x(), p.y());
  }

  private void set(Pt p, int value) {
    set(p.x(), p.y(), value);
  }

  private boolean isOnBoard(Pt p) {
    return p.x() >= 0 && p.x() < width && p.y() >= 0 && p.y() < height;
  }

  private boolean isUnvisited(Pt p) {
    return get(p) == EMPTY;
  }

  // offsets, not points
  private Supplier<Stream<Pt>> deltas() {
    return () -> Stream.of(
      Pt.of(-1, 0),
      Pt.of(0, -1),
      Pt.of(1, 0),
      Pt.of(0, 1)
    );
  }

  private Stream<Pt> neighbours(Pt p) {
    return deltas().get()
      .map(d -> p.move(d.x(), d.y()))
      .filter(this::isOnBoard);
  }

  private Stream<Pt> neighboursUnvisited(Pt p) {
    return neighbours(p)
      .filter(this::isUnvisited);
  }

  private Stream<Pt> neighboursByValue(Pt pt, int value) {
    return neighbours(pt)
      .filter(p -> get(p) == value);
  }

  private void initializeBoard(Set<Pt> obstacles) {
    obstacles.forEach(p -> set(p, OBSTACLE));
  }

  public Optional<Iterable<Pt>> trace(Pt src, Pt dst, Set<Pt> obstacles) {
    // 1. initialization
    initializeBoard(obstacles);
    System.out.println("2a");
    // 2. fill the board
    int[] counter = {START};
    set(src, counter[0]);
    counter[0]++;
    boolean found = false;
    for (Set<Pt> curr = Set.of(src); !(found || curr.isEmpty()); counter[0]++) {
      System.out.println(curr.size());
      System.out.println(boardFormatted(List.of()));
      Set<Pt> next = curr.stream()
        .flatMap(this::neighboursUnvisited)
        .collect(Collectors.toSet());
      next.forEach(p -> set(p, counter[0]));
      found = next.contains(dst);
      curr = next;
    }
    System.out.println("2b");
    // 3. backtrack (reconstruct path)
    if (!found) return Optional.empty();
    LinkedList<Pt> path = new LinkedList<>();
    path.add(dst);
    counter[0]--;
    Pt curr = dst;
    while (counter[0] > START) {
      counter[0]--;
      Pt prev = neighboursByValue(curr, counter[0])
        .findFirst()
//        .reduce((p1, p2) -> p2)
        .orElseThrow(() -> new RuntimeException("impossible"));
      path.addFirst(prev);
      curr = prev;
    }
    return Optional.of(path);
  }

  private String cellFormatted(Pt p, Set<Pt> path) {
    int value = get(p);
    String valueF = String.format("%3d", value);

    if (value == OBSTACLE) {
      Attribute a = new Attribute(Ansi.ColorFont.BLUE);
      return Colored.build(" XX", a);
    }

    if (path.isEmpty()) return valueF;

    if (path.contains(p)) {
      Attribute a = new Attribute(Ansi.ColorFont.RED);
      return Colored.build(valueF, a);
    }

    return valueF;
  }

  public String boardFormatted(Iterable<Pt> path0) {
    Set<Pt> path = StreamSupport
      .stream(path0.spliterator(), false)
      .collect(Collectors.toSet());
    return IntStream.range(0, height).mapToObj(y ->
      IntStream.range(0, width)
        .mapToObj(x -> Pt.of(x, y))
        .map(p -> cellFormatted(p, path))
        .collect(Collectors.joining())
    ).collect(Collectors.joining("\n"));
  }

  @Override
  public String toString() {
    return boardFormatted(Set.of());
  }

}
