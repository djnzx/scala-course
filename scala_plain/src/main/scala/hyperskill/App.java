package hyperskill;

import java.util.*;
import java.util.stream.*;

public class App {
  public static void main1(String[] args) {
    Scanner s = new Scanner(System.in);
    Stream.generate(s::next)
        .limit(4)
        .flatMap(x -> Arrays.stream(x.split(" ")))
        .collect(Collectors.toCollection(LinkedList::new))
        .descendingIterator()
        .forEachRemaining(System.out::println);
  }

  public static void main(String[] args) {
  }
}
