package topics.recursion.heap.java;

public class RecursionInHeapApp {


  public static void main(String[] args) {

    /** 1. implementation */
    Factorial f = new Factorial(20);

    /** 2. build the calculation */
    Deferred<Long> computation = f.build();

    /** 3. run the computation */
    Long outcome = computation.perform();

    System.out.println(outcome);
  }
}
