package az.be;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Parking {
  public static long carParkingRoof(List<Long> cars, int k) {
    List<Long> sorted = cars.stream().sorted().collect(Collectors.toList());
    return IntStream.rangeClosed(0, sorted.size() - k)
        .mapToLong(idx -> sorted.get(idx + k - 1) - sorted.get(idx) + 1)
        .min()
        .orElseThrow(RuntimeException::new);
  }

  public static void main(String[] args) {
    List<Long> cars = Arrays.asList(6L, 2L, 12L, 7L);
    long r = carParkingRoof(cars, 3);
    System.out.println(r); // 6
  }
}
// http://167.71.44.71/codenjoy-contest/board/player/vr70mxu5y8ussmvqp6s1?code=7213819288312594213
