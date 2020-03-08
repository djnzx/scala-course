package book_red.red07.async_basics;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

public class AsyncAppJava {
  // 1.
  // actually, consumer is no more than f: A => Unit
  interface NBFuture<A> {
    void apply(Consumer<A> consumer);
  }

  // 2
  // type definition
  interface Par<A> extends Function<ExecutorService, NBFuture<A>> { }

  // 3. submit to executor service any Runnable
  static void eval(ExecutorService es, Runnable body) {
    es.submit(body);
  }

  // 4. just lift function
  static <A> Par<A> unit(A a) {
    return new Par<A>() {
      @Override
      public NBFuture<A> apply(ExecutorService es) {
        return new NBFuture<A>() {
          @Override
          public void apply(Consumer<A> consumer) {
            consumer.accept(a);
          }
        };
      }
    };
  }

  // 5. simulating long-running task
  static <A> Par<A> sleep(Par<A> orig, long millis) {
    return new Par<A>() {
      @Override
      public NBFuture<A> apply(ExecutorService es) {
        try {
          System.out.println("sleeping");
          Thread.sleep(millis);
          System.out.println("\nawaking");
        } catch (InterruptedException e) {
          throw new RuntimeException("InterruptedException occurred", e);
        }
        return orig.apply(es);
      }
    };
  }

  // 6. forking our task by using eval
  static <A> Par<A> fork(Par<A> orig) {
    return new Par<A>() {
      @Override
      public NBFuture<A> apply(ExecutorService es) {
        return new NBFuture<A>() {
          @Override
          public void apply(Consumer<A> consumer) {
            Runnable body = new Runnable() {
              @Override
              public void run() {
                orig.apply(es).apply(consumer);
              }
            };
            eval(es, body);
          }
        };
      }
    };
  }

  public static void main(String[] args) throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);
    AtomicReference<Integer> ref = new AtomicReference<>();
    ExecutorService es = Executors.newFixedThreadPool(1);

    // constructing our async task
    Par<Integer> task = unit(7);
    Par<Integer> sleeping = sleep(task, 3000);
    Par<Integer> forked = fork(sleeping);
    NBFuture<Integer> future = forked.apply(es);

    // running our async task and wiring our data
    future.apply(ra -> {
      ref.set(ra);
      latch.countDown();
    });

    // while our task is calculating we are here
    while (latch.getCount() > 0) {
      System.out.print(".");
      Thread.sleep(100);
    }
    System.out.println();

    // we need this line if and only if
    // we don't have aly while with (latch.getCount() > 0)
    latch.await();
    int val = ref.get();
    System.out.printf("The value:%d\n", val);

    es.shutdown();
  }
}
