package se1;

import java.util.function.BiFunction;
import java.util.function.Function;

public class FunctionalTryCatch {

  // Function (int, int) => int
//   BiFunction<Integer, Integer, Integer>
  static int div(int a, int b) {
    return a / b;
  }

  static BiFunction<Integer, Integer, Integer> makeSafe(
    BiFunction<Integer, Integer, Integer> unSafe,
    Function<Exception, Integer> handler
  ) {
    return (a, b) -> {
      try {
        return unSafe.apply(a, b);
      } catch (Exception ex) {
        return handler.apply(ex);
      }
    };
  }

  static <A, B, C> BiFunction<A, B, C> makeSafeGen(
    BiFunction<A, B, C> unSafe,
    Function<Exception, C> handler
  ) {
    return (a, b) -> {
      try {
        return unSafe.apply(a, b);
      } catch (Exception ex) {
        return handler.apply(ex);
      }
    };
  }

  public static void main(String[] args) {
    int r1 = div(20,5); // 4
//    int r2 = div(20,0); // Exception

    BiFunction<Integer, Integer, Integer> safeDiv =
      makeSafe(FunctionalTryCatch::div, ex -> 13);

    Integer applied = safeDiv.apply(20, 0); // 13
  }


}
