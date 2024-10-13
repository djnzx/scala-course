package lesson35sql01.warmup;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BirdsMigrationApp {

  static int migratoryBirds(List<Integer> arr) {
    Map<Integer, Long> types = arr.stream()
        .collect(Collectors.groupingBy(a -> a, Collectors.counting()));

    long max_size = types.values().stream()
        .max(Comparator.comparingLong(a -> a))
        .orElseThrow(RuntimeException::new);

    return types.entrySet().stream()
        .filter(e -> e.getValue() == max_size)
        .map(Map.Entry::getKey)
        .min(Comparator.comparingInt(a->a))
        .orElseThrow(RuntimeException::new);
  }

}
