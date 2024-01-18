package hackerrank.d200319;

import java.util.function.IntConsumer;

/**
 * https://www.hackerrank.com/challenges/counting-valleys/problem
 */
public class CountingValleys {

  /**
   * naive iterative implementation
   */
  static int count1(int n, String s) {
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

  static class State {
    int level;
    int count;

    public State() {
      this(0,0);
    }

    public State(int level, int count) {
      this.level = level;
      this.count = count;
    }
  }

  /**
   * stream implementation with external state
   */
  static int count2(int n, String s) {
    State st = new State();

    IntConsumer processChar = c -> {
      int prev = st.level;
      switch (c) {
        case 'D': st.level--; break;
        case 'U': st.level++; break;
      }
      if (st.level==0 && prev<0) st.count++;
    };

    s.chars().forEach(processChar);
    return st.count;
  }

  /**
   * recursive implementation with state passing between iterations
   */
  static State process3(int idx, String s, State st) {
    if (idx==s.length()) return st;
    int prev = st.level;
    int nL = st.level + (s.charAt(idx) == 'D' ? -1 : 1);
    int nC = st.count + ((nL==0 && prev<0) ? 1 : 0);
    return process3(idx+1, s, new State(nL, nC));
  }

  static int count3(int n, String s) {
    return process3(0, s, new State()).count;
  }

  /**
   * recursive implementation without state just with variables
   */
  static int process4(int idx, String s, int level, int count) {
    if (idx==s.length()) return count;
    int nL = level + (s.charAt(idx) == 'U' ? 1 : -1);
    int nC = count + (nL==0 && level<0 ? 1 : 0);
    return process4(idx+1, s, nL, nC);
  }

  static int count4(int n, String s) {
    return process4(0, s, 0, 0);
  }

  /**
   * your solution answer is "How to write small tasks look like great project ?" question. :D
   */
  public static void main(String[] args) {
    String s = "UDDDUDUU";
    System.out.println(count1(s.length(), s));
    System.out.println(count2(s.length(), s));
    System.out.println(count3(s.length(), s));
    System.out.println(count4(s.length(), s));
  }
}
