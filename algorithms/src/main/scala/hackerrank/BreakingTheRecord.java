package hackerrank;

import java.util.List;

// https://www.hackerrank.com/challenges/breaking-best-and-worst-records/problem
public class BreakingTheRecord {

    public static List<Integer> breakingRecords(List<Integer> scores) {
        // Write your code here
        int best = scores.get(0);
        int worse = scores.get(0);
        int best_count = 0;
        int worse_count = 0;
        for (int x: scores) {
            if (x > best) {
                best = x;
                best_count += 1;
            } else if (x<worse) {
                worse = x;
                worse_count += 1;
            }
        }
        return List.of(best_count, worse_count);


    }

}
