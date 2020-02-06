package topics.recursion.java;

import static topics.recursion.java.TailCalls.call;

public class TailRecApp {

  public static TailCall<Integer> factorialTailRec(final int factorial, final int number) {
    if (number == 1)
      return TailCalls.done(factorial);
    else
      return call(() -> factorialTailRec(factorial * number, number - 1));
  }

  public static void main(String[] args) {
    System.out.println(factorialTailRec(1, 5).invoke());
  }
}
