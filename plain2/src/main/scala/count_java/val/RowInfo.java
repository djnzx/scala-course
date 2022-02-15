package count_java.val;

import count_java.util.Functions;

import java.util.Objects;

public final class RowInfo {
  public final int level;
  public final String name;
  public final long count;

  private final int LEVEL_SIZE = 2;

  public RowInfo(int level, String name, long count) {
    this.level = level;
    this.name = name;
    this.count = count;
  }

  @Override
  public String toString() {
    return String.format("%s%s : %d",
        Functions.indent(level, LEVEL_SIZE), name, count);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    if (!(o instanceof RowInfo)) return false;

    RowInfo that = (RowInfo) o;
    return level == that.level
        && count == that.count
        && Objects.equals(name, that.name);
  }

}

