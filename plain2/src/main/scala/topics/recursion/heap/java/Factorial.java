package topics.recursion.heap.java;

public class Factorial {

  private final long number;

  public Factorial(long number) {
    this.number = number;
  }

  /**
   * implementation is a bit different.
   * instead of calling return - we create an instance, holding the value
   * instead of making recursive step - we create suspended computation in the heap
   * <p>
   * it doesn't eliminate memory usage,
   * but it eliminates stack size problem,
   * because instances being created in the heap
   */
  private Deferred<Long> build(final long accum, final long number) {
    if (number == 1L)
      return // constructing the result
          new DeferredDone<>(accum);
    else
      return // constructing the next step
          new DeferredNext<>(() -> build(accum * number, number - 1));
  }

  public Deferred<Long> build() {
    return build(1, number);
  }

}
