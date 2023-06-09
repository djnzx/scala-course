package topics.recursion.heap.java;

import java.util.stream.Stream;

/**
 * the idea is to represent the next step
 * - whether it is a value
 * - whether it is a next step to calculate
 *
 * also, the Stream Nature makes it TAIL RECURSIVE
 */
public interface Deferred<A> {

  Deferred<A> apply();

  boolean isComplete();

  A result();

  default A perform() {
    return Stream
        // runs the stream in a tail recursive manner
        // our implementation is supposed to HOLD a result in the last element
        .iterate(this, Deferred::apply)
        // take the last value
        .reduce((next1, next2) -> next2)
        // just for safety ?
        .filter(Deferred::isComplete)
        .orElseThrow(() -> new IllegalStateException("Stream is supposed to terminal with DeferredDone"))
        .result();
  }
}
