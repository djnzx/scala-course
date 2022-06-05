package google.live.t0;

import java.util.List;
import java.util.Map;

/** interval is a class to represent interval on the timescale */
class Interval {
  final int a;
  final int b;

  public Interval(int a, int b) {
    this.a = a;
    this.b = b;
  }
}


public class IntervalsJava {

  /**
   * given: List<Interval>
   *
   *   1 ------------------------ 10
   *   1 ------- 4
   *       2 ----------- 7
   *               5 ---------- 8
   *   1 ---- 3
   *                     7 ---- 8
   *
   * task: spit & count all sub-intervals
   *
   * expected result:
   *
   *  1 -- 2  -> 3
   *  2 -- 3  -> 4
   *  3 -- 4  -> 3
   *  4 -- 5  -> 2
   *  5 -- 7  -> 3
   *  7 -- 8  -> 3
   *  8 -- 10 -> 1
   *
   */

  Map<Interval, Integer> splitAndCount(List<Interval> intervals) {
    throw new IllegalArgumentException("should be implemented");
  }


}
