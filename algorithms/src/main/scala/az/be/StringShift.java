package az.be;

public class StringShift {
  public static String shift(String orig, int toLeft, int toRight) {
    int len = orig.length();
    int total = (toRight - toLeft) % len;
    if (total == 0) return orig;

    int middle = (total > 0) ? len-total: -total;
    return orig.substring(middle)
        .concat(orig.substring(0, middle));
  }
}
