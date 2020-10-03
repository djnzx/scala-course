package google;

import java.util.*;
import java.util.stream.Collectors;

public class Task3Java {
  
  static boolean isNextTo(String word, String word2) {
    return Task31Levenstein.distance(word, word2) == 1;
  }
  
  static List<String> dict = Arrays.asList(
      "i", "in", "ik", "sin", "sing", "sting", "string",
      "like", "ike", "ide", "id", "king", "idea", "ikea",
      "a", "ab", "abc", "ink", "kin", "king", "kind"
  );
  
  static Collection<String> nextWords(String word) {
    return dict.stream()
        .filter(w -> w.length() == word.length() + 1)
        .filter(w -> isNextTo(word, w))
        .collect(Collectors.toList());
  }

  static List<List<String>> allSeq(String currWord) {
    Collection<String> nextWords = nextWords(currWord);
    if (nextWords.isEmpty()) return Arrays.asList(Collections.emptyList());

    return nextWords.stream()
        .flatMap(w -> allSeq(w).stream()
            .map((List<String> ls) -> {
              LinkedList<String> ll = new LinkedList<>(ls);
              ll.addFirst(w);
              return ll;
            })
        )
        .collect(Collectors.toList());
  }

  public static void main(String[] args) {
    List<List<String>> outcome = allSeq("");
    outcome.forEach(System.out::println);
  }

}
