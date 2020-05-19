package codewars.l8;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SpinWords {
  public String spinWords(String sentence) {
    return Arrays.stream(sentence.split(" "))
        .map(s -> s.length()>=5 ? new StringBuffer(s).reverse().toString() : s)
        .collect(Collectors.joining(" "));
  }
}