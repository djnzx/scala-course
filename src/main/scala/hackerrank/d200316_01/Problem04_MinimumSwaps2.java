package hackerrank.d200316_01;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Problem04_MinimumSwaps2 {

  static void proc(Map<Integer, Integer> process) {
    for (Map.Entry<Integer, Integer> el : process.entrySet()) {
      int index1 = el.getKey();
      int value1 = el.getValue();
      System.out.printf("i1:%d, v1:%d\n", index1, value1);
      int index2 = value1;
      if (process.containsKey(index2)) {
        int value2 = process.get(index2);
        System.out.printf("i2:%d, v2:%d\n", index2, value2);
        if (index1 == value2) {
          process.remove(index2);
          System.out.println("x2");
        }
      }
      process.remove(index1);
      break;
    }
  }

  static int minimumSwaps(int[] arr) {
    System.out.println(arr.length);
    HashMap<Integer, Integer> process = new HashMap<>(arr.length);
    int min = Arrays.stream(arr).min().orElse(1);
    for (int index = 0; index < arr.length; index++) {
      int value = arr[index];
      if (value != index + min) process.put(index + min, value);
    }
    System.out.println(process.size());
    int steps = 0;
    while (process.size() > 2) {
      System.out.println(process);
      proc(process);
      System.out.printf("size: %d\n", process.size());
      steps++;
      System.out.printf("steps: %d\n", steps);
    }
    if (process.size() < 2) return 0;
    return steps + 1;
  }

  public static void main(String[] args) {
    int[] data2 = {7, 1, 3, 2, 4, 5, 6};
//    int[] data = {2, 3, 4, 1, 5};
    int[] data = {8,45,35,84,79,12,74,92,81,82,61,32,36,1,65,44,89,40,28,20,97,90,22,87,48,26,56,18,49,71,23,34,59,54,14,16,19,76,83,95,31,30,69,7,9,60,66,25,52,5,37,27,63,80,24,42,3,50,6,11,64,10,96,47,38,57,2,88,100,4,78,85,21,29,75,94,43,77,33,86,98,68,73,72,13,91,70,41,17,15,67,93,62,39,53,51,55,58,99,46};
    int r = minimumSwaps(data);
    System.out.printf("The answer is: %d\n", r);
  }
}
