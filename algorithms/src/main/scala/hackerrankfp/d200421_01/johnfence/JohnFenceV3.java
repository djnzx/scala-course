package hackerrankfp.d200421_01.johnfence;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class JohnFenceV3 {

  static LinkedList<Integer> stack = new LinkedList<>();
  static int maxArea = 0;
  static int idx;

  static int max(int oldMax, int width, int height) {
    return Math.max(oldMax, width * height);
  }

  static int calcArea(int oldMax, int idx, LinkedList<Integer> s, int topHeight) {
    int width = s.isEmpty() ? idx : idx-1 -s.peekFirst();
    return max(oldMax, width, topHeight);
  }

  static int maxFence(List<Integer> fence) {
    // first fold (idx, maxArea, stack )
    for (idx = 0; idx < fence.size();) {
      if (stack.isEmpty() || fence.get(stack.peekFirst()) < fence.get(idx)) {
        stack.addFirst(idx++);
        System.out.printf("%2d : %24s : add\n", idx, stack);
//        idx++;
      } else {
        int h = fence.get(stack.removeFirst());
        maxArea = calcArea(maxArea, idx, stack, h);
        System.out.printf("%2d : %24s : H=%d : M=%2d\n", idx, stack, h, maxArea);
      }
    }

    System.out.println("--");
    // second fold (idx, maxArea, stack)
    // calculate to back
    while (!stack.isEmpty()) {
      int h = fence.get(stack.removeFirst());
      maxArea = calcArea(maxArea, idx, stack, h);
      System.out.printf("%2d : %24s : H=%d : M=%2d\n", idx, stack, h, maxArea);
    }

    return maxArea;
  }

  public static void main(String[] args) {
    System.out.println(maxFence(Arrays.asList(
        // idx:  0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18
        // values
//                 1,2,3,4,5,6,5,4,3,0, 4, 5, 6, 7, 8, 6, 3, 2
        1,2,3,4,5,6,7,8,6,4,2
    )));
  }
}
