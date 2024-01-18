package hackerrank.d200316;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * https://www.hackerrank.com/challenges/minimum-swaps-2/problem
 */
public class MinimumSwapsApp {

  static class Pair<A, B> {
    final A a;
    final B b;

    Pair(A a, B b) {
      this.a = a;
      this.b = b;
    }
  }

  static void makeStep(Map<Integer, Integer> process) {
    for (Map.Entry<Integer, Integer> el : process.entrySet()) {
      int i1 = el.getKey();
      int v1 = el.getValue();
      int v2 = process.get(v1);
      process.remove(i1);
      process.remove(v1);
      if (i1!=v2) process.put(i1, v2);
      break;
    }
  }

  static int minimumSwaps(int[] arr) {
    int min = Arrays.stream(arr).min().orElse(1);
    Map<Integer, Integer> process = IntStream.range(0, arr.length).boxed()
        .flatMap(idx -> {
          int valAt = arr[idx];
          int index = idx + min;
          return valAt == index ? Stream.empty() : Stream.of(new Pair<>(index, valAt));
        }).collect(Collectors.toMap(p -> p.a, p -> p.b));
    int steps = 0;
    while (!process.isEmpty()) {
      makeStep(process);
      steps++;
    }
    return steps;
  }

  public static void main(String[] args) {
    int[] data = {8,45,35,84,79,12,74,92,81,82,61,32,36,1,65,44,89,40,28,20,97,90,22,87,48,26,56,18,49,71,23,34,59,54,14,16,19,76,83,95,31,30,69,7,9,60,66,25,52,5,37,27,63,80,24,42,3,50,6,11,64,10,96,47,38,57,2,88,100,4,78,85,21,29,75,94,43,77,33,86,98,68,73,72,13,91,70,41,17,15,67,93,62,39,53,51,55,58,99,46};
    int r = minimumSwaps(data);
    System.out.printf("The answer is: %d\n", r);
  }
}
