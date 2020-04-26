package hackerrank.d200421_07;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class PasswordAppJava {
  static int p = 131;
  static long M = 1_000_000_000L+7;

  static int f(char c) {
    return c;
  }

  static BigDecimal pow(long a, long b) {
    BigDecimal ba = new BigDecimal(a);
    BigDecimal r = new BigDecimal(1);
    for (long i = 0; i < b; i++)
      r = r.multiply(ba);
    return r;
  }

  static long h(String pass) {
    BigDecimal r = new BigDecimal(0);
    int len = pass.length();
    for (int idx = 0; idx < pass.length(); idx++)
      r = r.add(
          pow(p, len-1-idx).multiply(new BigDecimal(f(pass.charAt(idx))))
      );
    return r.remainder(new BigDecimal(M)).longValue();
  }

  static boolean isValid1(String pass, long hash) {
    return hash == h(pass);
  }

  static boolean isValid2(String part, long hash) {
    return IntStream.rangeClosed('0', 'z').mapToObj(i -> part+(char)i)
        .anyMatch(s -> isValid1(s, hash));
  }

  static class Current {
    long hash;
    String passwd;
  }

  public static List<Integer> authEvents(List<List<String>> events) {
    LinkedList<Integer> r = new LinkedList<>();
    Current curr = new Current();

    events.forEach(event -> {
      String type = event.get(0);
      String data = event.get(1);
      if ("setPassword".equals(type)) {
        curr.hash = h(data);
        curr.passwd = data;
      } else {
        long hash = Long.parseLong(data);
        boolean valid = curr.hash == hash || isValid2(curr.passwd, hash);
        r.add(valid?1:0);
      }
    });

    return r;
  }

  public static void main(String[] args) {
    List<List<String>> data = Arrays.asList(
        Arrays.asList("setPassword", "000A"),
        Arrays.asList("authorize", "108738450"),
        Arrays.asList("authorize", "108738449"),
        Arrays.asList("authorize", "244736787")
    );
    System.out.println(authEvents(data));
    System.out.println(h("000A"));
//    System.out.println("223691457");
//    System.out.println(h("cAr1"));
//    System.out.println(isValid("000A", 108738450));
//    System.out.println(isValid("000A", 108738449)); //t
//    System.out.println(isValid("000A", 244736787)); //t
    System.out.println(Long.MAX_VALUE);
    System.out.println(pow(131,9));
    System.out.println(Math.pow(131, 9));
  }
}
