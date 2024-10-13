package lesson40w07.warmup;

import java.util.Arrays;

public class FindDigitsApp {

  static int[] split(int n) {
    int[] digits = new int[20];
    int cnt=0;
    while (n>0) {
      int r = n % 10;
      if (r!=0) digits[cnt++]=r;
      n /= 10;
    }
    return Arrays.copyOf(digits, cnt);
  }

  static int findDigits(int n) {
    return (int)Arrays.stream(split(n))
        .filter(d -> n % d == 0)
        .count();
  }

  static int findDigits2(int n) {
    return (int) Integer.toString(n).chars()
        .map(c -> c - '0')
        .filter(d -> n % d == 0)
        .count();
  }


}
