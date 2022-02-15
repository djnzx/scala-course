package count_java.val;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class FNode {
  public final File file;
  public final List<FNode> children;

  private FNode(File file, List<FNode> children) {
    this.file = file;
    this.children = children;
  }

  public static final class FFile extends FNode {
    public FFile(File file) {
      super(file, Collections.emptyList());
    }
  }

  public static final class FFolder extends FNode {
    public FFolder(File file, List<FNode> children) {
      super(file, children);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    if (getClass() != o.getClass()) return false;

    FNode that = (FNode) o;
    return Objects.equals(file, that.file)
        && Objects.equals(children, that.children);
  }

}

