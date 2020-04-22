package hackerrank.d200319_04;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Problem03_PileOfSocks {


  public int count(int n, int[] data) {

    return Arrays.stream(data).boxed()
        .collect(Collectors.groupingBy(x -> x))
        .values().stream().map(ls -> ls.size() / 2)
        .reduce(Integer::sum).orElse(0);
  }
}
