package interview.booking;

public class Problem1 {

  public static String getShiftedStringV1(String orig, int leftShifts, int rightShifts) {
    int len = orig.length();
    int total = (rightShifts - leftShifts) % len;
    if (total > 0) {
      String part1 = orig.substring(0, len - total);
      String part2 = orig.substring(len - total);
      return part2.concat(part1);
    }
    if (total < 0) {
      String part1 = orig.substring(0, -total);
      String part2 = orig.substring(-total);
      return part2.concat(part1);
    }
    return orig;
  }

  public static String getShiftedStringV2(String orig, int leftShifts, int rightShifts) {
    int len = orig.length();
    int total = (rightShifts - leftShifts) % len;
    if (total == 0) return orig;
    int middle = (total > 0) ? len-total : -total;
    return orig.substring(middle)
        .concat(orig.substring(0, middle));
  }
}
