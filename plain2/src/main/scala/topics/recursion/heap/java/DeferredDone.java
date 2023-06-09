package topics.recursion.heap.java;

public class DeferredDone<A> implements Deferred<A> {

  private final A a;

  public DeferredDone(A a) {
    this.a = a;
  }

  @Override
  public boolean isComplete() {
    return true;
  }

  @Override
  public A result() {
    return a;
  }

  @Override
  public Deferred<A> apply() {
    throw new IllegalStateException("this instance is supposed TO HOLD the value only");
  }

}
