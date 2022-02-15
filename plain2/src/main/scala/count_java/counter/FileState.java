package count_java.counter;

public final class FileState {
  public final long count;
  public final boolean inBlock;

  private FileState(long count, boolean inBlock) {
    this.count = count;
    this.inBlock = inBlock;
  }

  public static FileState fresh() {
    return new FileState(0, false);
  }

  public FileState updated(int delta) {
    return updated(delta, inBlock);
  }

  public FileState updated(int delta, boolean inBlock) {
    return new FileState(count + delta, inBlock);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    if (!(o instanceof FileState)) return false;
    FileState that = (FileState) o;
    return this.count == that.count
        && this.inBlock == that.inBlock;
  }
}
