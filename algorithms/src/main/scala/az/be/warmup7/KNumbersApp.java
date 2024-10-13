package lesson45s03.warmup;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class KNumbersApp {

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
    return new Pair<>(
        ns.substring(0, ns.length() - at),
        ns.substring(ns.length() - at)
    );
  }

  static long stoi(String s) {
    return s.isEmpty() ? 0 : Long.parseLong(s);
  }

  static Pair<Long, Long> ptoi(Pair<String, String> p) {
    return new Pair<>(stoi(p.a), stoi(p.b));
  }

  private static final String INVALID = "INVALID RANGE";

  static void kaprekarNumbers(int p, int q) {
    if (p<=0 || p>=100000 || p>=q) System.out.println(INVALID);
    else {
      String result = IntStream.rangeClosed(p, q)
          .mapToObj(n -> new Pair<>(n, ptoi(split(sq(n), len(n)))))
          .filter(pa -> pa.a == pa.b.a + pa.b.b)
          .map(pa -> pa.a)
          .map(pa -> itos(pa))
          .collect(Collectors.joining(" "));
      System.out.println(result.isEmpty() ? INVALID : result);
    }
  }
}
