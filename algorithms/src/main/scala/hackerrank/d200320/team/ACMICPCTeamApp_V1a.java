package hackerrank.d200320.team;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ACMICPCTeamApp_V1a {

  static class Pair<A, B> {
    final A a;
    final B b;

    Pair(A a, B b) {
      this.a = a;
      this.b = b;
    }
  }

  static List<Integer> findCommon(String[] topics, int i1, int i2) {
    return IntStream.range(0, topics[0].length())
        .filter(idx -> topics[i1].charAt(idx)=='1' || topics[i2].charAt(idx)=='1')
        .boxed()
        .collect(Collectors.toList());
  }

  static int[] acmTeam(String[] topics) {
    // all possible combination
    int len = topics.length;
    List<Pair<Integer, Integer>> permutations =
        IntStream.range(0, len).boxed().flatMap(a ->
            IntStream.range(a + 1, len).mapToObj(b -> new Pair<>(a, b)))
            .collect(Collectors.toList());

    List<Integer> data = permutations.stream()
        .map(p -> new Pair<>(p, findCommon(topics, p.a, p.b).size()))
        .filter(p -> p.b > 0)
        .map(p -> p.b)
        .collect(Collectors.toList());

    int maxT = data.stream().max((a,b)->a-b).orElseThrow(RuntimeException::new);
    int maxG = (int) data.stream().filter(n -> n == maxT).count();
    return new int[] { maxT, maxG };
  }

}
