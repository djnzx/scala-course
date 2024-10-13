package lesson59.warmup;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskManagerApp {

  // structure to hold the data
  public static class Node {
    public int value;
    public List<Node> children = new ArrayList<>();
  }

  // structure to hold the tree
  // ...

  // structure to hold the tree navigation
  // ...

  public static Command parse(String raw) {
    if (raw.equals("print")) return new CmdPrint();
    if (raw.startsWith("change")) return new CmdChangeValue(raw);
    if (raw.startsWith("visit child")) return new CmdVisitChild(raw);
    // ...
    throw new RuntimeException("shouldn't be there");
  }

  public static Optional<Integer> process(Command cmd) {
    throw new RuntimeException("should be implemented");
  }

  public static List<Integer> filterResults(List<Optional<Integer>> data) {
    throw new RuntimeException("should be implemented");
  }

  public static List<String> readCommandsFromConsole(InputStream in) {
    throw new RuntimeException("should be implemented");
  }

  public static void print(List<Integer> data, OutputStream out) {
    throw new RuntimeException("should be implemented");
  }

  public static void main(String[] args) {
    Command cmd = new CmdVisitChild(6);
  }

}
