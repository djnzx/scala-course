package hackerrank.d200319_04;

import java.util.function.IntConsumer;

/**
 * https://www.hackerrank.com/challenges/counting-valleys/problem
 */
public class CountingValleys {

  static int count(int n, String s) {
    int level = 0;
    int count = 0;
    int prev = 0;
    for (int i = 0; i < n; i++) {
      char c = s.charAt(i);
      switch (c) {
        case 'D': prev = level; level--; break;
        case 'U': prev = level; level++; break;
      }
      if (level==0 && prev<0) count++;
    }
    return count;
  }

  static int count2(int n, String s) {
    class State {
      int level = 0;
      int count = 0;
      int prev = 0;
    }

    State st = new State();

    IntConsumer processChar = c -> {
      switch (c) {
        case 'D': st.prev = st.level; st.level--; break;
        case 'U': st.prev = st.level; st.level++; break;
      }
      if (st.level==0 && st.prev<0) st.count++;
    };

    s.chars().forEach(processChar);
    return st.count;
  }

  /**
   * your solution answer is "How to write small tasks look like great project ?" question. :D
   */
  public static void main(String[] args) {
    System.out.println(count(8, "UDDDUDUU"));
  }
}
