package topics.recursion.heap.java;

import java.util.function.Supplier;

public class DeferredNext<A> implements Deferred<A> {

  private final Deferred<A> nextCall;

  public DeferredNext(Supplier<Deferred<A>> nextCall) {
    this.nextCall = nextCall.get();
  }

  @Override
  public Deferred<A> apply() {
    return nextCall;
  }

  @Override
  public boolean isComplete() {
    return false;
  }

  @Override
  public A result() {
    throw new IllegalStateException("this instance is supposed TO REPRESENT suspended computation only");
  }

}
