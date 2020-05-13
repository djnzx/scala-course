package hackerrank.d200320_05.ladybugs;

public class LadyBugsApp_V1 {

  static String squeeze(String s) {
    if (s.isEmpty()) return s;
    StringBuilder sb = new StringBuilder();
    char c0 = s.charAt(0);
    sb.append(c0);
    for (int i = 1; i < s.length(); i++) {
      char ci = s.charAt(i);
      if (ci != c0) {
        sb.append(ci);
        c0 = ci;
      }
    }
    return sb.toString();
  }

  static boolean alreadyOrdered(String s) {
    String squeezed = squeeze(s);
    return (squeezed.length() == squeezed.codePoints().distinct().count());
  }

  static boolean hasSingles(String b) {
    return b.codePoints()
        .filter(c -> c != '_')
        .distinct()
        .anyMatch(c -> b.codePoints().filter(c1 -> c1 == c).count() == 1);
  }

  static String happyLadybugs(String b) {
    if (hasSingles(b)) return "NO";
    final boolean hasEmptyCell = b.contains("_");
    if (b.codePoints().filter(c -> c < '_')
        .distinct()
        .allMatch(c -> hasEmptyCell && b.codePoints().filter(c1 -> c1 == c).count() > 1)) return "YES";
    if (alreadyOrdered(b.replace("_",""))) return "YES";
    return "NO";
  }

  public static void main(String[] args) {
    System.out.println(happyLadybugs("BBB"));
  }

}
