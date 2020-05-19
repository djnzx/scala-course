package codewars.l8;

import java.util.stream.IntStream;

public class DigiPow {

  private static int[] split(int n) {
    int[] digits = new int[10];
    int count = 0;
    while (n > 0) {
      int digit = n % 10;
      int remain = n / 10;
      digits[count++] = digit;
      n = remain;
    }
    int[] digits2 = new int[count];
    for (int i = 0; i < count; i++) {
      digits2[i]=digits[count-i-1];
    }
    return digits2;
  }

  private static long mult(int[] digits, int p) {
    return IntStream.range(0, digits.length).map(d -> (int)Math.pow(digits[d], p+d)).sum();
  }

  private static long mult2(int[] digits, int p) {
    long r = 0;
    for (int i = 0; i < digits.length; i++) {
      r = r + (long)Math.pow(digits[i], p+i);
    }
    return r;
  }

  public static long digPow(int n, int p) {
    int[] ditits = split(n);
    long res = mult(ditits, p);
    int remain = (int)(res % n);
    return remain == 0 ? res / n : -1;
  }

}
