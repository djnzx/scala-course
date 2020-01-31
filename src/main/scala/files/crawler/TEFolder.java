package files.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static files.crawler.Fn.tab;

public class TEFolder extends TreeElement {
  public final List<TreeElement> items;

  public TEFolder(String path, int level) {
    this(path, level, new ArrayList<>());
  }

  public TEFolder(String path, int level, List<TreeElement> items) {
    super(path, level);
    this.items = items;
  }

  @Override
  public String toString() {
//    String h = String.format("%s[%s]", tab(level), path);
//    String h = String.format("[%s]", path);
    String h2 = items.stream().map(Object::toString).filter(s -> !s.isEmpty()).collect(Collectors.joining("\n"));
//    return h2.equals("") ? h : String.format("%s\n%s", h, h2);
    return h2;
  }
}
