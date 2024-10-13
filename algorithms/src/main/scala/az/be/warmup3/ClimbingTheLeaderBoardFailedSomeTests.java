package lesson41w08.warmup;

import java.util.Arrays;

public class ClimbingTheLeaderBoardFailedSomeTests {

  static int[] climbingLeaderboard(int[] scores, int[] alice) {
    return Arrays.stream(alice).map(a -> {
      int[] aaa = Arrays.stream(scores).filter(s0 -> s0 >= a).distinct().toArray();
      if (aaa.length==0) return 1;
      if (aaa[aaa.length-1]==a) return aaa.length;
      else return aaa.length+1;
    }).toArray();
  }

}
