package interview.booking;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Problem2 {

  public static long carParkingRoof(List<Long> cars, int k) {
    List<Long> sorted = cars.stream().sorted().collect(Collectors.toList());
    return IntStream.rangeClosed(0, cars.size() - k)
        .mapToLong(idx -> sorted.get(idx + k - 1) - sorted.get(idx) + 1)
        .min().orElseThrow(() -> new RuntimeException("shouldn't be here"));
  }

}
