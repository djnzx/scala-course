package files.crawler;

import java.util.Collections;

public class Fn {
  static String tab(int level) {
    return String.join("", Collections.nCopies(level*4, " "));
  }

  static String lastpart(String path) {
    String[] split = path.split("/");
    return split[split.length-1];
  }

}
