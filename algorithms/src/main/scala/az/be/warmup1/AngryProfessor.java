package lesson39w06.warmup;

import java.util.Arrays;

public class AngryProfessor {

  static String angryProfessor(int k, int[] a) {
    return Arrays.stream(a).filter(t -> t <= 0).count() >= k ? "YES" : "NO";
  }

}
