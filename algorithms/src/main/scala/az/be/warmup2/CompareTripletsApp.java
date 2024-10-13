package lesson40w07.warmup;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class CompareTripletsApp {
  static List<Integer> compareTriplets1(List<Integer> a, List<Integer> b) {
    int cnt1=0;
    int cnt2=0;
    for (int i = 0; i < a.size(); i++) {
      cnt1 += a.get(i) > b.get(i) ? 1 : 0;
      cnt2 += a.get(i) < b.get(i) ? 1 : 0;
    }
    return Arrays.asList(cnt1, cnt2);
  }

  static class Summer {
    int cnt1 = 0;
    int cnt2 = 0;
  }

  static List<Integer> compareTriplets2(List<Integer> a, List<Integer> b) {
    Summer s = new Summer();
    IntStream.range(0, a.size()).forEach(i->{
      // comparator
      s.cnt1 += a.get(i) > b.get(i) ? 1 : 0;
      s.cnt2 += a.get(i) < b.get(i) ? 1 : 0;
    });
    return Arrays.asList(s.cnt1, s.cnt2);
  }

  static class Pair {
    final int cnt1;
    final int cnt2;

    Pair(int cnt1, int cnt2) {
      this.cnt1 = cnt1;
      this.cnt2 = cnt2;
    }

    public List<Integer> toList() {
      return Arrays.asList(cnt1, cnt2);
    }
  }

  static List<Integer> compareTriplets3(List<Integer> a, List<Integer> b) {
    return IntStream.range(0, a.size()).mapToObj(i -> new Pair(
        a.get(i) > b.get(i) ? 1 : 0,
        a.get(i) < b.get(i) ? 1 : 0
    ))
        .reduce((p1, p2) -> new Pair(p1.cnt1 + p2.cnt1, p1.cnt2 + p2.cnt2))
        .orElseThrow(RuntimeException::new)
        .toList();
  }

  static List<Integer> compareTriplets4(List<Integer> a, List<Integer> b) {
    return IntStream.range(0, a.size()).mapToObj(i -> new Pair(
        a.get(i) > b.get(i) ? 1 : 0,
        a.get(i) < b.get(i) ? 1 : 0
    ))
        .reduce(
            new Pair(0,0),
            (p1, p2) -> new Pair(p1.cnt1 + p2.cnt1, p1.cnt2 + p2.cnt2))
        .toList();
  }




}
