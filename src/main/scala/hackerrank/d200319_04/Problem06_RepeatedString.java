package hackerrank.d200319_04;

public class Problem06_RepeatedString {
  static long count(String s, long n) {
    long a_count = s.chars().filter(c -> c == 'a').count();
    long rep = n / s.length();
    String residual = s.substring(0, (int) (n % s.length()));
    long b_count = residual.chars().filter(c -> c == 'a').count();
    return a_count*rep + b_count;
    // 1. LongStream.range(0, n).map(x -> x % s.length()).map(x -> s.charAt((int)x)).filter(c -> c == 'a').count();
    // 2.     long count = 0;
    //    for (int i = 0; i < n; i++) {
    //      int index = i % s.length();
    //      char letter = s.charAt(index);
    //      if (letter == 'a') count++;
    //    }
    //    return count;
  }

  public static void main(String[] args) {
    long r = count("a", 1000000000000L);
    System.out.println(r);
  }
}
