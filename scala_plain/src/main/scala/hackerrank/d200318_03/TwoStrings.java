package hackerrank.d200318_03;

import java.util.Set;
import java.util.stream.Collectors;

public class TwoStrings {
  
  static Set<Integer> toSet(String s) {
    return s.chars().boxed().collect(Collectors.toSet());
  }
  
  static String twoStrings(String s1, String s2) {
    Set<Integer> set1 = toSet(s1); 
    Set<Integer> set2 = toSet(s2);
    int ss = set1.size() + set2.size();
    set2.addAll(set1);

    return set2.size() < ss ? "YES" : "NO";
  }

}
