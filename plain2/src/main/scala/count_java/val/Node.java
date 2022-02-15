package count_java.val;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public abstract class Node {
  public final File file;
  public final int level;
  public final long count;
  public final List<Node> children;

  private Node(File file, int level, long count, List<Node> children) {
    this.file = file;
    this.level = level;
    this.count = count;
    this.children = children;
  }

  public static final class NFile extends Node {
    public NFile(File file, int level, Function<File, Long> counter) {
      super(file, level,
          counter.apply(file),
          Collections.emptyList());
    }
  }

  private static long sum(Collection<Node> data, Function<Node, Long> mapper) {
    return data.stream().map(mapper).reduce(Long::sum).orElse(0L);
  }

  public static final class NFolder extends Node {
    public NFolder(File file, int level, List<Node> children) {
      super(file, level,
          sum(children, n -> n.count),
          children);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    if (getClass() != o.getClass()) return false;

    Node that = (Node) o;
    return level == that.level
        && count == that.count
        && Objects.equals(file, that.file)
        && Objects.equals(children, that.children);
  }

}

