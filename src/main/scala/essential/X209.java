package essential;

import java.util.*;
import java.util.stream.Collectors;

public class X209 {
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

  private static <T> String combine(T subj, T verb, T obj) {
    return String.format("%s %s %s", subj, verb, obj);
  }

  private <T> List<String> process(List<T> subjects, List<T> verbs, List<T> objects) {
    ArrayList<String> total = new ArrayList<>();
    subjects.forEach(subj ->
        verbs.forEach(verb ->
            objects.forEach(obj ->
                total.add(combine(subj, verb, obj))
            )
        )
    );
    return total;
  }

  private <T> List<String> process2(List<T> subjects, List<T> verbs, List<T> objects) {
    return subjects.stream().flatMap(subj ->
        verbs.stream().flatMap(verb ->
            objects.stream().map(obj ->
                combine(subj, verb, obj))))
        .collect(Collectors.toList());
  }

  public static void main(String[] args) {
    List<String> subjects = list("Noel", "The cat", "The dog");
    List<String> verbs = list("wrote", "chased", "slept on");
    List<String> objects = list("the book", "the ball", "the bed");

    X209 app = new X209();
    List<String> sentences = app.process2(subjects, verbs, objects);
    sentences.forEach(System.out::println);
  }

}
/**
 Noel wrote the book
 Noel wrote the ball
 Noel wrote the bed
 Noel chased the book
 Noel chased the ball
 Noel chased the bed
 Noel slept on the book
 Noel slept on the ball
 Noel slept on the bed
 The cat wrote the book
 The cat wrote the ball
 The cat wrote the bed
 The cat chased the book
 The cat chased the ball
 The cat chased the bed
 The cat slept on the book
 The cat slept on the ball
 The cat slept on the bed
 The dog wrote the book
 The dog wrote the ball
 The dog wrote the bed
 The dog chased the book
 The dog chased the ball
 The dog chased the bed
 The dog slept on the book
 The dog slept on the ball
 The dog slept on the bed
*/
