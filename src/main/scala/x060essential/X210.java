package x060essential;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class X210 {
  static class MapEntry<K,V> {
    final K key;
    final V value;

    MapEntry(K key, V value) {
      this.key = key;
      this.value = value;
    }
  }

  @SafeVarargs
  private static <T> List<T> list(T... word) {
    return Arrays.asList(word);
  }

  private static <K, V> MapEntry<K,V> of(K key, V value) {
    return new MapEntry<>(key, value);
  }

  @SafeVarargs
  private static <K, V> Map<K, V> map(MapEntry<K, V>... items) {
    Map<K, V> map = new HashMap<>();
    Arrays.asList(items).forEach(itm -> map.put(itm.key, itm.value));
    return map;
  }

  private static Map<String, List<String>> assoc_subj_verb = map(
    of("Noel", list("wrote", "chased", "slept on")),
    of("The cat", list("meowed at", "chased", "slept on")),
    of("The dog", list("barked at", "chased", "slept on"))
  );

  private static Map<String, List<String>> assoc_verb_obj = map(
    of("wrote", list("the book", "the letter", "the code")),
    of("chased", list("the ball", "the dog", "the cat")),
    of("slept on", list("the bed", "the mat", "the train")),
    of("meowed at", list("Noel", "the door", "the food cupboard")),
    of("barked at", list("the postman", "the car", "the cat"))
  );

  public static void main(String[] args) {

  }

}
