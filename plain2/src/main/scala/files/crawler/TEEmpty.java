package files.crawler;

import static files.crawler.Fn.tab;

public class TEEmpty extends TreeElement {
  protected TEEmpty(String path, int level) {
    super(path, level);
  }

  @Override
  public String toString() {
//    return String.format("%sE: %s", tab(level), path);
    return String.format("E: %s", path);
  }
}
