package files.crawler;

public abstract class TreeElement {
  public final String path;
  public final int level;

  protected TreeElement(String path, int level) {
    this.path = path;
    this.level = level;
  }
}
