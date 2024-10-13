package lesson40w07.warmup;

import java.math.BigInteger;

public class XtraLonmgFactApp {

  static void extraLongFactorials(int n) {
    BigInteger result = BigInteger.valueOf(1);

    for (int i = 2; i <=n ; i++) {
      result =  result.multiply(BigInteger.valueOf(i));
    }

    System.out.println(result);
  }
}
