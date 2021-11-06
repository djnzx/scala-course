package files.crawler;

import static files.crawler.Fn.tab;

public class TEFile extends TreeElement {
  public final Long size;

  public TEFile(String path, int level, Long size) {
    super(path, level);
    this.size = size;
  }

  @Override
  public String toString() {
//    return String.format("%s%s, s:%s", tab(level), path, size);
    return String.format("F: %s, s:%s", path, size);
  }
}
