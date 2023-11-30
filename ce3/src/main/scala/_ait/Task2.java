package _ait;

import java.util.Arrays;
import java.util.HashMap;

public class Task2 {

  public static int ArrayChallenge(int[] arr) {
    HashMap<Integer, Integer> state = new HashMap<>();
    Arrays.stream(arr)
      .forEach(x -> {
          if (!state.containsKey(x)) state.put(x, 0);
          state.forEach((n, amount) -> {
            if (n < x) state.put(n, Math.max(amount, x - n));
          });
        }
      );
    return state.values().stream()
      .max(Integer::compareTo)
      .orElse(0);
  }

}
