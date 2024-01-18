package hackerrankfp.d200421.johnfence;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class JohnFenceV3a {

  static class IH {
    final int idx;
    final int height;

    IH(int idx, int height) {
      this.idx = idx;
      this.height = height;
    }

    @Override
    public String toString() {
      return String.format("[i:%d, h:%d]", idx, height);
    }
  }

  static LinkedList<IH> stack = new LinkedList<>();
  static int maxArea = 0;
  static int idx;

  /**
   * get rid of ONE fence and calc max
   * f: (oldMax, idx, stack) => (max, stack)
   */
  static int calcArea(int oldMax, int idx, LinkedList<IH> s) {
    int topHeight = s.removeFirst().height;
    int width = s.isEmpty() ? idx : idx-1 - s.peekFirst().idx;
    int max = Math.max(oldMax, width * topHeight);
    System.out.printf("M:%d\n", max);
    return max;
  }

  static int maxFence(List<Integer> fence) {

    for (idx = 0; idx < fence.size();) {
      int height = fence.get(idx);
      if (stack.isEmpty() || stack.peekFirst().height < height) {
        stack.addFirst(new IH(idx, height));
        idx++;
      } else {
        maxArea = calcArea(maxArea, idx, stack);
      }
    }

    while (!stack.isEmpty()) {
      maxArea = calcArea(maxArea, idx, stack);
    }

    return maxArea;
  }

  public static void main(String[] args) {
    System.out.println(maxFence(Arrays.asList(
        // idx:  0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18
        // values
//                 1,2,3,4,5,6,5,4,3,0, 4, 5, 6, 7, 8, 6, 3, 2
        1, 2, 3, 4, 5, 6, 7, 8, 6, 4, 2
//        1,2,3,4,5,6,7,8,6,4,2
    )));
  }
}
