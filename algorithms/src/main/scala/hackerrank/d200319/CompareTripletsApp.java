package hackerrank.d200319;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

/**
 * https://www.hackerrank.com/challenges/compare-the-triplets/problem
 */
public class CompareTripletsApp {

  /**
   * the biggest problem of that code - TWO externally mutated variable
   */
  static List<Integer> compareTriplets1(List<Integer> a, List<Integer> b) {
    int cnt1=0;
    int cnt2=0;
    for (int i = 0; i < a.size(); i++) {
      cnt1 += a.get(i) > b.get(i) ? 1:0;
      cnt2 += a.get(i) < b.get(i) ? 1:0;
    }
    return Arrays.asList(cnt1, cnt2);
  }

  /**
   * the biggest problem of that code - ONE externally mutated variable
   */
  static List<Integer> compareTriplets2(List<Integer> a, List<Integer> b) {
    Summer s = new Summer();
    IntStream.range(0, a.size()).forEach(i-> {
      s.cnt1 += a.get(i) > b.get(i) ? 1:0;
      s.cnt2 += a.get(i) < b.get(i) ? 1:0;
    });
    return Arrays.asList(s.cnt1, s.cnt2);
  }

  static class Summer {
    int cnt1;
    int cnt2;

    Summer() {
      this.cnt1 = 0;
      this.cnt2 = 0;
    }

    Summer(int cnt1, int cnt2) {
      this.cnt1 = cnt1;
      this.cnt2 = cnt2;
    }

    static Summer fromComp(int c) {
      return new Summer(c > 0 ? 1 : 0, c < 0 ? 1 : 0);
    }

    static Summer zero() {
      return new Summer(0, 0);
    }

    public List<Integer> toList() {
      return Arrays.asList(cnt1, cnt2);
    }
  }

  /**
   * the biggest problem of that code - DUPLICATED comparision of Objects
   */
  static List<Integer> compareTriplets3(List<Integer> a, List<Integer> b) {
    return IntStream.range(0, a.size()).mapToObj(i -> new Summer(
        a.get(i) > b.get(i) ? 1 : 0,
        a.get(i) < b.get(i) ? 1 : 0
    )).reduce((s1, s2) -> new Summer(s1.cnt1 + s2.cnt1, s1.cnt2 + s2.cnt2))
        .orElseThrow(RuntimeException::new)
        .toList();
  }

  /**
   * the best implementation
   */
  static List<Integer> compareTriplets4(List<Integer> a, List<Integer> b) {
    Comparator<Integer> cmp = Comparator.comparingInt(z->z);
    return IntStream.range(0, a.size()).mapToObj(i -> {
      int c = cmp.compare(a.get(i), b.get(i));
      return new Summer(c > 0 ? 1 : 0, c < 0 ? 1 : 0);
    }).reduce(
        new Summer(0, 0),
        (s1, s2) -> new Summer(s1.cnt1 + s2.cnt1, s1.cnt2 + s2.cnt2)
    )
        .toList();
  }

  /**
   * even better ;)
   */
  static List<Integer> compareTriplets5(List<Integer> a, List<Integer> b) {
    Comparator<Integer> cmp = Comparator.comparingInt(i -> i);
    return IntStream.range(0, a.size())
        .mapToObj(i ->
            Summer.fromComp(cmp.compare(a.get(i), b.get(i)))
        )
        .reduce(
            Summer.zero(),
            (s1, s2) -> new Summer(s1.cnt1 + s2.cnt1, s1.cnt2 + s2.cnt2)
        )
        .toList();
  }

}
