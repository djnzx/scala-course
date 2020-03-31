package warmup;

import java.math.BigDecimal;

public class XtraLong {

  public static void fact(int n) {
    BigDecimal r = new BigDecimal(1);
    for (int i = 1; i <= n; i++) {
      r = r.multiply(new BigDecimal(i));
    }
    System.out.println(r.toString());
  }

}
