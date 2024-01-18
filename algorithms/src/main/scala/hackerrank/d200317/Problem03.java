package hackerrank.d200317;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Problem03 {

  static double median5(int[] data, int from, int tail) {
//    System.out.println(Arrays.toString(Arrays.copyOfRange(data, from, from + tail)));
    return (double) IntStream.range(from, from+tail).map(idx -> data[idx]).sum() / tail;
  }

  static class Pair {
    public final double med;
    public final int val;

    Pair(double med, int val) {
      this.med = med;
      this.val = val;
    }
  }

  static int activityNotifications(int[] expenditure, int d) {
    return (int) IntStream.range(0, expenditure.length - d).mapToObj(idx -> new Pair(median5(expenditure, idx, d), expenditure[idx + d]))
        .filter(p -> p.val >= p.med * 2)
//        .peek(p -> System.out.printf("%d %d", p.med, p.val))
        .count();
  }

  public static void main(String[] args) throws IOException {
    String content = Files.lines(Paths.get("./src/main/java/hackerrank/x03sort/input01.txt")).collect(Collectors.joining());
    System.out.println(content);
    int[] d = Arrays.stream(content.split(" ")).mapToInt(Integer::parseInt).toArray();
//    int[] d = {2, 3, 4, 2, 3, 6, 8, 4, 5};
//    int[] d = {1, 2, 3, 4, 4};
    int r = activityNotifications(d, 10000); // 200000 10000
    System.out.printf("The answer is: %d\n", r);
  }
}
