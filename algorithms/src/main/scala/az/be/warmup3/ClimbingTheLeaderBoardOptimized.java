package lesson41w08.warmup;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ClimbingTheLeaderBoardOptimized {

  static class Score {
    final int val;

    Score(int val) {
      this.val = val;
    }
  }

  // binary search already implemented in the Arrays.binarySearch()
  static int calc_score(List<Score> scores, int alice) {
    int idx = Collections.binarySearch(scores, new Score(alice), (o1, o2) -> o2.val-o1.val);
    if (idx > 0) return idx+1;
    if (idx == 0) return 1;
    return Math.abs(idx);
  }

  static int[] climbingLeaderboard(int[] scores, int[] alice) {
    // get rid of duplicates
    List<Score> scores_distinct = Arrays.stream(scores)
        .distinct()
        .mapToObj(Score::new)
        .collect(Collectors.toList());
    // logic
    return Arrays.stream(alice)
        .map(a -> calc_score(scores_distinct, a))
        .toArray();
  }

}
