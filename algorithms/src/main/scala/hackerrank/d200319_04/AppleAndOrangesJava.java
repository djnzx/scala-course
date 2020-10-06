package hackerrank.d200319_04;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

/**
 * https://www.hackerrank.com/challenges/apple-and-orange/problem
 */
public class AppleAndOrangesJava {

  static void countApplesAndOranges1(int s, int t, int a, int b, int[] apples, int[] oranges) {
    long ac = Arrays.stream(apples).map(x -> x + a).filter(p -> p >= s && p <= t).count();
    long oc = Arrays.stream(oranges).map(x -> x + b).filter(p -> p >= s && p <= t).count();
    System.out.printf("%d\n%d\n", ac, oc);
  }

  static void countApplesAndOranges2(int s, int t, int a, int b, int[] apples, int[] oranges) {
    IntPredicate ip = p -> p >= s && p <= t;
    long ac = Arrays.stream(apples).map(x -> x + a).filter(ip).count();
    long oc = Arrays.stream(oranges).map(x -> x + b).filter(ip).count();
    System.out.printf("%d\n%d\n", ac, oc);
  }

  static void countApplesAndOranges3(int s, int t, int a, int b, int[] apples, int[] oranges) {
    IntPredicate ip = p -> p >= s && p <= t;
    IntUnaryOperator plusa = x -> x + a;
    IntUnaryOperator plusb = x -> x + b;
    long ac = Arrays.stream(apples).map(plusa).filter(ip).count();
    long oc = Arrays.stream(oranges).map(plusb).filter(ip).count();
    System.out.printf("%d\n%d\n", ac, oc);
  }

  static void countApplesAndOranges4(int s, int t, int a, int b, int[] apples, int[] oranges) {
    IntPredicate ip = p -> p >= s && p <= t;
    Function<Integer, IntUnaryOperator> plus = y -> x -> x + y;
    long ac = Arrays.stream(apples).map(plus.apply(a)).filter(ip).count();
    long oc = Arrays.stream(oranges).map(plus.apply(b)).filter(ip).count();
    System.out.printf("%d\n%d\n", ac, oc);
  }

  static void countApplesAndOranges5(int s, int t, int a, int b, int[] apples, int[] oranges) {
    IntPredicate ip = p -> p >= s && p <= t;
    Function<Integer, IntUnaryOperator> plus = y -> x -> x + y;
    BiFunction<int[], IntUnaryOperator, Long> count =
        (ints, f) -> Arrays.stream(ints).map(f).filter(ip).count();
    long ac = count.apply(apples, plus.apply(a));
    long oc = count.apply(oranges, plus.apply(b));
    System.out.printf("%d\n%d\n", ac, oc);
  }

}
