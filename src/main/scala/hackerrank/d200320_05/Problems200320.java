package hackerrank.d200320_05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Problems200320 {

  static class Pair<A, B> {
    final A a;
    final B b;

    Pair(A a, B b) {
      this.a = a;
      this.b = b;
    }
  }

  static long sq(long n) {
    return n*n;
  }

  static String itos(long n) {
    return Long.toString(n);
  }

  static long stoi(String s) {
    if (s.isEmpty()) return 0;
    return Long.parseLong(s);
  }

  static int len(int n) {
    return itos(n).length();
  }

  static Pair<String, String> split(long nn, int size) {
    String ss = itos(nn);
    String s1 = ss.substring(0, ss.length()-size);
    String s2 = ss.substring(ss.length()-size);
    return new Pair<>(s1, s2);
  }

  static Pair<Long, Long> ptoi(Pair<String, String> p) {
    return new Pair<>(stoi(p.a), stoi(p.b));
  }

  static void kaprekarNumbers(int p, int q) {
    if (p<=0 || p>=100000 || p>=q) System.out.println("INVALID RANGE");
    else {
      String nums = IntStream.rangeClosed(p, q)
          .mapToObj(n -> new Pair<>(n, ptoi(split(sq(n), len(n)))))
          .filter(pa -> pa.a == pa.b.a + pa.b.b)
          .map(pa -> pa.a)
          .map(n -> itos(n))
          .collect(Collectors.joining(" "));
      if (nums.isEmpty()) nums="INVALID RANGE";
      System.out.println(nums);
    }
  }

  static byte[][] stoa(String[] strings) {
    int len = strings.length;
    byte[][] data = new byte[len][len];
    for (int i = 0; i < len; i++) {
      for (int j = 0; j < len; j++) {
        data[i][j] = (byte) (strings[i].charAt(j) - '0');
      }
    }
    return data;
  }

  static String[] represent(byte[][] a) {
    String[] res = new String[a.length];
    byte[] bytes = new byte[a.length];
    for (int i = 0; i < a.length; i++) {
      for (int j = 0; j < a.length; j++)
        bytes[j] = (byte) (a[i][j] < 0 ? 'X' : a[i][j] +'0');
      res[i] = new String(bytes);
    }
    return res;
  }

  static boolean isCavityAt(byte[][] data, int x, int y) {
    int c = data[y+1][x+1];
    return c > data[y][x+1] &&
        c > data[y+1][x] && c > data[y+1][x+2]
        && c > data[y+2][x+1];
  }

  static String[] cavityMap(String[] grid) {
    if (grid.length < 3) return grid;
    byte[][] a = stoa(grid);
    IntStream.rangeClosed(0, a.length-3).boxed().flatMap(dx ->
        IntStream.rangeClosed(0, a.length-3).mapToObj(dy ->
            new Pair<>(new Pair<>(dx, dy), isCavityAt(a, dx, dy))
        ))
        .filter(p -> p.b)
        .map(p -> p.a)
        .forEach(p -> a[p.b+1][p.a+1] = -1);
    return represent(a);
  }

  static List<Integer> next(List<Integer> curr, int a, int b) {
    return curr.stream().flatMap(n -> Stream.of(n+a, n+b)).distinct().collect(Collectors.toList());
  }

  static int[] stones(int n, int a, int b) {
    List<Integer> step = Arrays.asList(a, b);
    while (--n>1) {
      step = next(step, a, b);
    }
    return step.stream().mapToInt(x->x).distinct().sorted().toArray();
  }

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

  public static void main3(String[] args) {
    System.out.println(happyLadybugs("BBB"));
  }

  public static void main2(String[] args) {
    System.out.println(Arrays.toString(stones(2,1,1)));
  }

  public static void main1(String[] args) throws IOException {
    String[] grid = Files.lines(Paths.get("100.txt")).toArray(String[]::new);
    String[] r = cavityMap(grid);
    for (String s: r) {
      System.out.println(s);
    }
    Files.write(Paths.get("my.txt"), Arrays.asList(r));
  }

  // all even
  static boolean isDone(int[] amount) {
    return Arrays.stream(amount).allMatch(n -> (n & 1) == 0);
  }

  // only one odd
  static boolean isWrongState(int[] amount) {
    return Arrays.stream(amount).filter(n -> (n & 1) == 1).count() == 1;
  }

  // find the 1st
  static int findTheOdd(int[] bread) {
    for (int i = 0; i < bread.length; i++) {
      if ((bread[i]&1) ==1) return i;
    }
    throw new IllegalArgumentException("must be!");
  }

  static Optional<Integer> fairRations(int[] bread) {
    int step=0;
    while (!isDone(bread) && !isWrongState(bread)) {
      int idx = findTheOdd(bread);
      bread[idx]++;
      bread[idx+1]++;
      step++;
    }
    if (isDone(bread)) return Optional.of(step*2);
    return Optional.empty();
  }

  public static void main(String[] args) {
    Optional.of(1).map(n -> Integer.toString(n)).orElse("NO");
    int[] d = {2, 3, 4, 5, 6};
    System.out.println(fairRations(d));
  }

}
