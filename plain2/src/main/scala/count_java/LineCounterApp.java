package count_java;

import java.io.File;

public class LineCounterApp {

  public static void exit(String message) {
    System.err.println(message);
    System.exit(-1);
  }

  private static File validate(String[] args) {
    if (args.length == 0) exit("file or folder name is expected as a parameter to run the app");
    File path = new File(args[0]);
    if (!path.exists()) exit("valid file or folder name is expected as a parameter to run the app");
    return path;
  }

  public static void body(String[] args) {
    System.out.println(args[0]);
    File root_path = validate(args);
    Configuration conf = new Configuration();
    new LineCounter(conf::fileFilter, conf.counter()::count)
        .processParallel(root_path)
        .forEach(System.out::println);
  }

  public static void main(String[] args) {
    body(new String[]{"/Users/alexr/dev/_learn"});
  }
}
