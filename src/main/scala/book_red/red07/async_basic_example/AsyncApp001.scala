package book_red.red07.async_basic_example

import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.{Callable, CountDownLatch, ExecutorService, Executors}

object AsyncApp001 extends App {

  trait Detached[+A] {
    def apply(callback: A => Unit): Unit
  }

  type Par[A] = ExecutorService => Detached[A]

  def unit[A](a: A): Par[A] = _ => new Detached[A] {
    override def apply(callback: A => Unit): Unit = callback(a)
  }

  def sleep[A](pa: Par[A], time: Long): Par[A] = es => {
    Thread.sleep(time)
    pa(es)
  }

  // run any callback in the another thread given by ExecutorService
  def eval(es: ExecutorService)(callback: => Unit): Unit = {
    val c: Callable[Unit] = () => callback
    val _ = es.submit(c)
  }

  def detach[A](pa: => Par[A]): Par[A] = es => new Detached[A] {
    def apply(cb: A => Unit): Unit = eval(es) {
      pa(es) { cb } // dive into callback
    }
  }

  def doWhile(cond: => Boolean)(action: => Unit): Unit =
    while (cond) action

  // description only
  val task: Par[Int] = unit(7)
  val taskSleeping: Par[Int] = sleep(task, 2000)
  val taskSleepingForked: Par[Int] = detach(taskSleeping)

  // preparation
  val es: ExecutorService = Executors.newFixedThreadPool(1)

  // place in the heap where we will store our result
  val ref = new AtomicReference[Int]()
  // latch to be sure that we are ready to extract value
  val latch = new CountDownLatch(1)
  // right here we only passed Executor service to our task
  // and got instance of Detached which is aware of ES
  val detached: Detached[Int] = taskSleepingForked(es)
  // and only here we run our task
  detached { a =>
    ref.set(a)
    latch.countDown()
  }
  // I can do whatever I want
  doWhile(latch.getCount > 0) {
    print(".")
    Thread.sleep(100)
  }
  println
  latch.await()
  val rez: Int = ref.get()
  println(s"Reference got: $rez")

  es.shutdown()
}
