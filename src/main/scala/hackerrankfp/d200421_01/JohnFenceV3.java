package hackerrankfp.d200421_01;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class JohnFenceV3 {

  static LinkedList<Integer> stack = new LinkedList<>();
  static int area;
  static int maxArea = 0;
  static int idx;

  static void calcAreaAndUpdate(List<Integer> fence, int top) {
    int width = stack.isEmpty() ? idx : idx-stack.peekFirst()-1;
    area = fence.get(top) * width;
    maxArea = Math.max(area, maxArea);
  }

  static int maxFence(List<Integer> fence) {
    maxArea = 0;
    stack.clear();

    // first fold (idx, maxArea, stack )
    for (idx = 0; idx < fence.size();)
      if (stack.isEmpty() || fence.get(stack.peekFirst()) < fence.get(idx)) {
        stack.addFirst(idx++);
      } else {
        calcAreaAndUpdate(fence, stack.removeFirst());
      }

    // second fold (idx, maxArea, stack)
    while (!stack.isEmpty())
      calcAreaAndUpdate(fence, stack.removeFirst());

    return maxArea;
  }

  public static void main(String[] args) {
    System.out.println(maxFence(Arrays.asList(
        2,5,7,4,1,8
    )));
  }
}
