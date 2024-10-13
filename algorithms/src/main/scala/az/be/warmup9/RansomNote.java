package lesson58sd1.warmup;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class RansomNote {
  static void checkMagazine(String[] magazine, String[] note) {
    Map<String, Long> words = Arrays.stream(magazine)
        .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

    boolean ok = Arrays.stream(note)
        .allMatch(w -> words.merge(w, -1L, Long::sum) >= 0);

    System.out.println(ok ? "Yes" : "No");
  }
}
