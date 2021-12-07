package se1;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class FunctionalDesign {

  void printLine(String line) {
    System.out.println(line);
  }

  void printLine(String line, PrintStream out) {
    out.println(line);
  }

  int add(int x, int y) {
    return x + y;
  }

  public String readLine() {
    Scanner s = new Scanner(System.in);
    String line = s.nextLine();
    return line;
  }

  public String readLine(InputStream in) {
    Scanner s = new Scanner(in);
    String line = s.nextLine();
    return line;
  }

  public static void main1(String[] args) {
    List<Integer> ints = Arrays.asList(1, 2, 3);
    List<String > strings = Arrays.asList("a", "b", "c");

//    ints.stream().flatMap(i ->
//        strings.stream().flatMap(s -> {
//          int a = i;
//          String s1 = s;
//        })
//    )



  }

  public static void main(String[] args) {
    int x = 5;
    String s = Integer.toString(x);
    x = x + 10;
    System.out.println(s);

    Optional.of(5)
        .map(z -> z + 10)
        .map(z -> Integer.toString(z))
        .ifPresent(z -> System.out.println(z));


  }


}
