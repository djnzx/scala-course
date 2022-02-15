package count_java;

import count_java.count.CountNonBlank;
import count_java.counter.StripAndCount;
import count_java.strip.Strip5Wisely;

import java.io.File;

public class Configuration {

  public StripAndCount counter() {
    return new StripAndCount(
        new Strip5Wisely(),
        new CountNonBlank()
    );
  }

  public boolean fileFilter(File fl) {
    return fl.isFile()
        && fl.toString().toLowerCase().endsWith(".java");
  }

}
