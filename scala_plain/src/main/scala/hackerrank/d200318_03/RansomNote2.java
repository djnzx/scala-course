package hackerrank.d200318_03;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class RansomNote2 {
  static Map<String, Long> toMap(String[] data) {
    return Arrays.stream(data)
        .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
  }

  static void checkMagazine(String[] magazine, String[] note) {
    Map<String, Long> iHave = toMap(magazine);
    Map<String, Long> iNeed = toMap(note);

    boolean ok = iNeed.keySet().stream().allMatch(w ->
        iHave.getOrDefault(w, 0L) >= iNeed.get(w)
    );

    System.out.println(ok ? "Yes" : "No");
  }
}
