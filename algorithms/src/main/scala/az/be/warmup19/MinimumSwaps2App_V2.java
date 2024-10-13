package lesson46s04.warmup;

import lombok.ToString;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MinimumSwaps2App_V2 {

  @ToString
  static class Pair<A, B> {
    final A a;
    final B b;

    Pair(A a, B b) {
      this.a = a;
      this.b = b;
    }
  }

  private static void makeStep(Map<Integer, Integer> process) {
    for (Map.Entry<Integer, Integer> el: process.entrySet()) {
      int i1 = el.getKey();
      int v1 = el.getValue();
      int v2 = process.get(v1);
      process.remove(i1);
      process.remove(v1);
      if (i1 != v2) process.put(i1, v2);
      break;
    }
  }

  static int minimumSwaps(int[] arr) {
    Map<Integer, Integer> process = IntStream.range(0, arr.length).boxed()
        .flatMap(idx -> {
          int valAt = arr[idx];
          int index = idx + 1;
          return index == valAt ? Stream.empty() :
              Stream.of(new Pair<>(index, valAt));
        }).collect(Collectors.toMap(p -> p.a, p -> p.b));
    int steps=0;
    while (!process.isEmpty()) {
      makeStep(process);
      steps++;
    }
    return steps;
  }

  public static void main(String[] args) {
    System.out.println(minimumSwaps(new int[]{7, 1, 3, 2, 4, 5, 6}));
  }

}
