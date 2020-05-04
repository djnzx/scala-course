package hackerrank.d200319_04;

import java.time.LocalTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import static java.util.stream.Collectors.toList;

public class Warmups {
  static void staircase(int n) {
    String s = IntStream.range(0, n).mapToObj(row ->
        IntStream.range(0, n).mapToObj(col ->
            col < n-row-1 ? " " : "#")
            .collect(Collectors.joining()))
        .collect(Collectors.joining("\n"));
    System.out.println(s);
  }

  static void plusMinus(int[] arr) {
    int np = 0;
    int nn = 0;
    int nz = 0;
    for (int el: arr) {
      if (el>0) np++;
      else if (el==0) nz++;
      else nn++;
    }
    double p = (double) np / arr.length;
    double z = (double) nz / arr.length;
    double n = (double) nn / arr.length;
    System.out.printf("%7f\n%7f\n%7f", p, n, z);
  }

  public static int diagonalDifference1(List<List<Integer>> data) {
    int s1 = 0;
    int s2 = 0;
    for (int i = 0; i < data.size(); i++) {
      List<Integer> line = data.get(i);
      s1 += line.get(i);
      s2 += line.get(line.size() - i - 1);
    }
    return Math.abs(s1 - s2);
  }

  public static int diagonalDifference2(List<List<Integer>> data) {
    return Math.abs(
        IntStream.range(0, data.size()).map(i -> data.get(i).get(i)).sum() -
        IntStream.range(0, data.size()).map(i -> data.get(i).get(data.size()-1-i)).sum()
    );
  }

  public static int diagonalDifference3(List<List<Integer>> data) {
    class Pair {
      final int a;
      final int b;

      Pair(int a, int b) {
        this.a = a;
        this.b = b;
      }
    }
    return IntStream.range(0, data.size()).mapToObj(i -> new Pair(
        data.get(i).get(i),
        data.get(i).get(data.size() - 1 - i)
    ))
        .reduce((p1, p2) -> new Pair(p1.a + p2.a, p1.b + p2.b))
        .map(p -> Math.abs(p.a - p.b))
        .orElseThrow(RuntimeException::new);
  }

  static long aVeryBigSum(long[] ar) {
    return Arrays.stream(ar).sum();
  }

  static class Pair<A, B> {
    final A a;
    final B b;

    Pair(A a, B b) {
      this.a = a;
      this.b = b;
    }
  }

  static void miniMaxSum(int[] arr) {
    LongSummaryStatistics ss =
        LongStream.range(0, arr.length).map(i ->
            LongStream.range(0, arr.length).mapToObj(idx -> new Pair<>(idx, arr[(int)idx]))
                .filter(p -> p.a != i).mapToLong(p -> p.b).sum()
        )
            .summaryStatistics();
    System.out.printf("%d %d", ss.getMin(), ss.getMax());
  }

  static int birthdayCakeCandles(int[] ar) {
    int max = Arrays.stream(ar).max().orElse(Integer.MAX_VALUE);
    return (int) Arrays.stream(ar).filter(v -> v == max).count();
  }

  static String timeConversion(String s) {
    return LocalTime.parse(s, DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.US)).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
  }

  /**
   * https://www.hackerrank.com/challenges/grading/problem
   */
  public static List<Integer> gradingStudents(List<Integer> grades) {
    return grades.stream().map(g ->
      g < 38 ? g : g % 5 > 2 ? (g / 5 + 1) * 5 : g
    ).collect(toList());
  }

  static int gcd(int a, int b) {
    int max = Math.max(a, b);
    int min = Math.min(a, b);
    int r = max % min;
    if (r == 0) return min;
    return gcd(min, r);
  }

  public static int getTotalX(List<Integer> a, List<Integer> b) {
    int max = a.stream().max(Comparator.comparingInt(z -> z)).orElse(Integer.MAX_VALUE);
    int min = b.stream().min(Comparator.comparingInt(z -> z)).orElse(Integer.MIN_VALUE);
    return (int) IntStream.rangeClosed(max, min)
        .mapToObj(n -> new Pair<>(n, a.stream().allMatch(a1 -> n % a1 == 0)))
        .filter(p -> p.b)
        .mapToInt(p -> p.a)
        .mapToObj(n -> new Pair<>(n, b.stream().allMatch(b1 -> b1 % n == 0)))
        .filter(p -> p.b)
        .mapToInt(p -> p.a)
        .count();
  }

  static class Keeper {
    int most_count;
    int least_count;
    int most_value;
    int least_value;

    public Keeper(int most_count, int least_count, int most_value, int least_value) {
      this.most_count = most_count;
      this.least_count = least_count;
      this.most_value = most_value;
      this.least_value = least_value;
    }
  }

  static int[] breakingRecords(int[] scores) {
    Keeper k = new Keeper(0, 0, scores[0], scores[0]);
    Arrays.stream(scores).skip(1).forEach(score -> {
      if (score > k.most_value) {
        k.most_count++;
        k.most_value=score;
      } else if (score < k.least_value) {
        k.least_count++;
        k.least_value=score;
      }
    });
    return new int[]{k.most_count, k.least_count};
  }

  static int divisibleSumPairs(int n, int k, int[] ar) {
    return (int) IntStream.range(0, ar.length).mapToObj(idx -> new Pair<>(idx, ar[idx])).flatMap(p1 ->
        IntStream.range(0, ar.length).mapToObj(idx -> new Pair<>(idx, ar[idx])).map(p2 ->
            new Pair<>(p1, p2)))
        .filter(p -> p.a.a < p.b.a)
        .filter(p -> (p.a.b + p.b.b) % k == 0)
        .count();
  }

  static int birthday(List<Integer> s, int d, int m) {
    return (int) IntStream.range(0, s.size()-m)
        .map(start -> IntStream.range(start, start+m).map(s::get).sum())
        .filter(x -> x == d)
        .count();
  }

  /**
   * https://www.hackerrank.com/challenges/migratory-birds/problem
   */
  static int migratoryBirds(List<Integer> arr) {
    Map<Integer, Long> types = arr.stream()
        .collect(Collectors.groupingBy(a -> a, Collectors.counting()));

    long max_size = types.values().stream()
        .max(Comparator.comparingLong(a -> a))
        .orElseThrow(RuntimeException::new);

    return types.entrySet().stream()
        .filter(e -> e.getValue() == max_size)
        .map(Map.Entry::getKey)
        .min(Comparator.comparingInt(a->a))
        .orElseThrow(RuntimeException::new);
  }

  static String dayOfProgrammer(int year) {
    boolean leapYear = IsoChronology.INSTANCE.isLeapYear(year);
    int[] before1918 = {31,29,31,30,31,30,31,31,30,31,30,31};
    int[] after1918  = {31,28,31,30,31,30,31,31,30,31,30,31};
    int[] after1918L = {31,29,31,30,31,30,31,31,30,31,30,31};
    int[] m;
    if (year < 1919) m = before1918;
    else if (leapYear) m = after1918L;
    else m = after1918;
    int prog=256;
    int month=0;
    while (prog>0) {
      prog -= m[month++];
    }
//    System.out.println(month);
//    System.out.println(prog+m[month]-1);
    return String.format("%d.%02d.%d\n", prog+m[month]-1, month, year);
//    return LocalDate.ofYearDay(year, 256).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
  }

  static int[] climbingLeaderboard(int[] scores, int[] alice) {
    return Arrays.stream(alice).map(a -> {
      int[] aaa = Arrays.stream(scores).filter(s0 -> s0 >= a).distinct().toArray();
      if (aaa.length==0) return 1;
      if (aaa[aaa.length-1]==a) return aaa.length;
      else return aaa.length+1;
    }).toArray();
  }

  static int[] cutTheSticks(int[] arr) {
    ArrayList<Integer> amount = new ArrayList<>();
    while (true) {
      // calc
      int curr_count = (int) Arrays.stream(arr).filter(x -> x > 0).count();
      if (curr_count == 0) break;
      amount.add(curr_count);
      // subtract
      int toCut = Arrays.stream(arr).filter(x -> x > 0).min().orElse(0);
      for (int i = 0; i < arr.length; i++) {
        arr[i] -= toCut;
      }
    }
    return amount.stream().mapToInt(x->x).toArray();
  }

  static int chocolateFeast(int initial, int price1, int exch) {
    int eaten = 0;
    // money spent
    int chock = initial / price1;
    // has wrappers
    int w = 0;
    // loop
    while (chock>0) {
      eaten += chock;
      w += chock;
      chock = w / exch;
      w %= exch;
    }
    return eaten;
  }

  // 5 failed
  static long strangeCounter5fail(long t) {
    int len = 3;
    while (true) {
      int counter = len;
      if (t<counter) {
        while (counter>0) {
          t--;
          if (t==0) return counter;
          counter--;
        }
      } else
        t -= counter;
      len *= 2;
    }
  }

  static long strangeCounter(long t) {
    for (long len = 3;; len <<= 1) {
      if (t == len) return 1;
      else if (t > len) t -= len;
      else return len - t + 1;
    }
  }

  public static void main(String[] args) {
    int[] rec = {5, 4, 4, 2, 2, 8};
    int[] res = cutTheSticks(rec);
    System.out.println(Arrays.toString(res));
  }
}
