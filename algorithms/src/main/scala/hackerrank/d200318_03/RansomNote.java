package hackerrank.d200318_03;

import java.util.*;
import java.util.stream.Collectors;

/**
 * https://www.hackerrank.com/challenges/ctci-ransom-note/problem
 */
public class RansomNote {

  static void checkMagazine(String[] magazine, String[] note) {
    
    Map<String, Long> words = Arrays.stream(magazine)
        .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
    
    boolean ok = Arrays.stream(note)
        .allMatch(word -> words.merge(word, -1L, Long::sum) >= 0);
    
    System.out.println(ok? "Yes" : "No");
  }

  public static void main(String[] args) {
    int r = 0;
    System.out.printf("The answer is: %d\n", r);
  }
}
