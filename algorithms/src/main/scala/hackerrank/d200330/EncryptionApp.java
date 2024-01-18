package hackerrank.d200330;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EncryptionApp {
  static String encryption(String s0) {
    final String s = s0.replace(" ", "");
    int len = s.length();
    double l = Math.sqrt(len);
    final int l1 = (int) Math.floor(l);
    final int l2 = (int) Math.ceil(l);
    final int cols = l2;
    final int rows = l1 == l2 ? l2 : l1 * l2 < len ? l2 : l1;

    return IntStream.range(0, cols).boxed().flatMap(c ->
        IntStream.range(0, rows).mapToObj(r -> {
          int idx = r * cols + c;
          String ch = idx < len ? String.valueOf(s.charAt(idx)) : "";
          return r==rows-1 ? ch +" ": ch;
        })).collect(Collectors.joining());
  }

  public static void main(String[] args) {
//    System.out.println(encryption("have a nice day"));
    System.out.println(encryption("feed the dog"));
//    System.out.println(encryption("chillout"));
  }
}
