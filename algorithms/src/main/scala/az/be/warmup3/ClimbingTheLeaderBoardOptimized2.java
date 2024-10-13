package lesson41w08.warmup;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ClimbingTheLeaderBoardOptimized2 {

  // binary search already implemented in the Arrays.binarySearch()
  static int calc_score(Integer[] scores, int alice) {
    int idx = Arrays.binarySearch(scores, alice, (o1, o2) -> o2-o1);
    if (idx < 0) return Math.abs(idx);
    return idx+1;
  }

  static int[] climbingLeaderboard(int[] scores, int[] alice) {
    // get rid of duplicates
    Integer[] scores_distinct = Arrays.stream(scores)
        .distinct()
        .boxed()
        .toArray(Integer[]::new);
    // logic
    return Arrays.stream(alice)
        .map(a -> calc_score(scores_distinct, a))
        .toArray();
  }

}
