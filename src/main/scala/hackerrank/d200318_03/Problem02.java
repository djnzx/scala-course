package hackerrank.d200318_03;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Problem02 {
  static String twoStrings(String s1, String s2) {
    Set<Integer> set1 = s1.chars().boxed().collect(Collectors.toSet());
    Set<Integer> set2 = s2.chars().boxed().collect(Collectors.toSet());
    Set<Integer> set3 = new HashSet<>(set1);
    set3.addAll(set2);

    return set3.size() < set1.size() + set2.size() ?
    "YES" : "NO";
  }
  public static void main(String[] args) {
    int r = 0;
    System.out.printf("The answer is: %d\n", r);
  }
}
