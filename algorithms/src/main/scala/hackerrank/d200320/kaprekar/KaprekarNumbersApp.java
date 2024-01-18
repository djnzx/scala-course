package hackerrank.d200320.kaprekar;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class KaprekarNumbersApp {

  static class Pair<A, B> {
    final A a;
    final B b;

    Pair(A a, B b) {
      this.a = a;
      this.b = b;
    }
  }

  static long sq(long n) {
    return n * n;
  }

  static String itos(long n) {
    return Long.toString(n);
  }

  static int len(int n) {
    return itos(n).length();
  }

  static Pair<String, String> split(long n, int at) {
    String ns = itos(n);
    String left = ns.substring(0, ns.length() - at);
    String right = ns.substring(ns.length() - at);
    return new Pair<>(left, right);
  }

  static long stoi(String s) {
    return s.isEmpty() ? 0 : Long.parseLong(s);
  }

  static Pair<Long, Long> ptoi(Pair<String, String> p) {
    return new Pair<>(stoi(p.a), stoi(p.b));
  }

  static void kaprekarNumbers(int p, int q) {
    if (p<=0 || p>=100000 || p>=q) System.out.println("INVALID RANGE");
    else {
      String nums = IntStream.rangeClosed(p, q)
          .mapToObj(n -> new Pair<>(n, ptoi(split(sq(n), len(n)))))
          .filter(pa -> pa.a == pa.b.a + pa.b.b)
          .map(pa -> pa.a)
          .map(n -> itos(n))
          .collect(Collectors.joining(" "));
      System.out.println(nums.isEmpty() ? "INVALID RANGE" : nums);
    }
  }

}
