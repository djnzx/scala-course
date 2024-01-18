package hackerrank.d200320.team;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ACMICPCTeamApp_V3 {

  static class Pair<A, B> {
    final A a;
    final B b;

    Pair(A a, B b) {
      this.a = a;
      this.b = b;
    }
  }

  static int findCommon(String[] topics, int i1, int i2) {
    return (int) IntStream.range(0, topics[0].length())
        .mapToObj(idx -> topics[i1].charAt(idx)=='1' || topics[i2].charAt(idx)=='1')
        .filter(x -> x == true)
        .count();
  }

  static int[] acmTeam(String[] topics) {
    // all possible combination
    int len = topics.length;
    List<Pair<Integer, Integer>> permutations =
        IntStream.range(0, len).boxed().flatMap(a ->
            IntStream.range(a + 1, len).mapToObj(b -> new Pair<>(a, b)))
            .collect(Collectors.toList());

    List<Integer> data = permutations.stream()
        .map(p -> findCommon(topics, p.a, p.b))
        .filter(n -> n > 0)
        .collect(Collectors.toList());

    int maxT = data.stream().max((a,b)->a-b).orElseThrow(RuntimeException::new);
    int maxG = (int) data.stream().filter(n -> n == maxT).count();
    return new int[] { maxT, maxG };
  }

}
