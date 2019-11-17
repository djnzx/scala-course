package x060essential;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

  private static <T> String combine(T subj, T verb, T obj) {
    return String.format("%s %s %s", subj, verb, obj);
  }

  private <T> List<String> process(Map<T, List<T>> assoc_subj_verb, Map<T, List<T>> assoc_verb_obj) {
    ArrayList<String> total = new ArrayList<>();
    assoc_subj_verb.forEach((subj, verbs) ->
        verbs.forEach(verb ->
            assoc_verb_obj.get(verb).forEach(obj ->
                total.add(combine(subj, verb, obj)))
        )
    );
    return total;
  }

  private <T> List<String> process2(Map<String, List<String>> assoc_subj_verb, Map<String, List<String>> assoc_verb_obj) {
    return assoc_subj_verb.entrySet().stream()        // Stream<String, List<String>> === Stream<Subj, List<Verb>>
        .flatMap(s_v ->                                // s_v.getKey() -> '1 Subj', sv.getValue() -> List<Verb>
            s_v.getValue().stream()                   // Stream<String> === Stream<Verb>
                .flatMap(verb ->                       // '1 Verb'
                    assoc_verb_obj.get(verb).stream() // Stream<String> === Stream<Obj>
                        .map(obj ->                   // '1 Obj'
                            combine(s_v.getKey(), verb, obj))
                )
        ).collect(Collectors.toList());
  }

  private <T> List<String> process3(Map<String, List<String>> assoc_subj_verb, Map<String, List<String>> assoc_verb_obj) {
    return assoc_subj_verb.entrySet().stream().flatMap(s_v ->
        s_v.getValue().stream().flatMap(verb ->
                assoc_verb_obj.get(verb).stream().map(obj ->
                    combine(s_v.getKey(), verb, obj)))
        ).collect(Collectors.toList());
  }

  public static void main(String[] args) {
    Map<String, List<String>> assoc_subj_verb = map(
        of("Noel", list("wrote", "chased", "slept on")),
        of("The cat", list("meowed at", "chased", "slept on")),
        of("The dog", list("barked at", "chased", "slept on"))
    );

    Map<String, List<String>> assoc_verb_obj = map(
        of("wrote", list("the book", "the letter", "the code")),
        of("chased", list("the ball", "the dog", "the cat")),
        of("slept on", list("the bed", "the mat", "the train")),
        of("meowed at", list("Noel", "the door", "the food cupboard")),
        of("barked at", list("the postman", "the car", "the cat"))
    );

    X210 app = new X210();
    List<String> sentences = app.process2(assoc_subj_verb, assoc_verb_obj);
    sentences.forEach(System.out::println);
  }

}
/**
 Noel wrote the book
 Noel wrote the letter
 Noel wrote the code
 Noel chased the ball
 Noel chased the dog
 Noel chased the cat
 Noel slept on the bed
 Noel slept on the mat
 Noel slept on the train
 The cat meowed at Noel
 The cat meowed at the door
 The cat meowed at the food cupboard
 The cat chased the ball
 The cat chased the dog
 The cat chased the cat
 The cat slept on the bed
 The cat slept on the mat
 The cat slept on the train
 The dog barked at the postman
 The dog barked at the car
 The dog barked at the cat
 The dog chased the ball
 The dog chased the dog
 The dog chased the cat
 The dog slept on the bed
 The dog slept on the mat
 The dog slept on the train
*/
