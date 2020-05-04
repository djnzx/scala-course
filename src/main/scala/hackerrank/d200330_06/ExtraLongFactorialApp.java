package hackerrank.d200330_06;

import java.math.BigDecimal;

/**
 * https://www.hackerrank.com/challenges/extra-long-factorials/problem
 */
public class ExtraLongFactorialApp {

  public static void xtralongfact(int n) {
    BigDecimal r = new BigDecimal(1);
    for (int i = 1; i <= n; i++) {
      r = r.multiply(new BigDecimal(i));
    }
    System.out.println(r.toString());
  }

}
