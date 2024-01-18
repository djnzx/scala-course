package hackerrank.d200317;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Problem02 {
  static int maximumToys(int[] prices, int k) {
    List<Integer> pr = Arrays.stream(prices).sorted().boxed().collect(Collectors.toList());
    System.out.println(pr);
    int count = 0;
    int i=0;
    while (k>0 && i<pr.size()) {
      k -= pr.get(i++);
      count++;
    }
    return k>=0 ? count : count -1;
  }

  public static void main(String[] args) {
    int d[] = {1, 12, 5, 111, 200, 1000, 10};

    int r = maximumToys(d, 50);
    System.out.printf("The answer is: %d\n", r);
  }
}
