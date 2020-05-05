package fp_red.red07.async_basics

import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.{Callable, CountDownLatch, ExecutorService, Executors}

object AsyncAppScala extends App {
  // 1. our result representation
  // we need to feed our representation with callback
  // which will describe what to do with that result
  trait Something[+A] {
    def apply(callback: A => Unit): Unit
  }

  // 2. our computation description
  type Par[A] = ExecutorService => Something[A]

  // 3. submit callback to the another thread given by ExecutorService
  def eval(es: ExecutorService)(callback: => Unit): Unit = {
    val c: Callable[Unit] = () => callback
    val _ = es.submit(c)
  }

  // 4. implementation for already existed value
  def unit[A](a: A): Par[A] = _ =>
    (callback: A => Unit) => callback(a)

  // 5. implementation for delay already represented Par[A]
  def sleep[A](pa: Par[A], time: Long): Par[A] = es => {
    println("sleeping")
    Thread.sleep(time)
    println("\nawaking")
    pa(es)
  }

  // 6. detach implementation for current thread already represented
  def detach[A](pa: => Par[A]): Par[A] = es => new Something[A] {
    def apply(cb: A => Unit): Unit = eval(es) {
      pa(es) { cb } // dive into callback
    }
  }

  def doWhile(cond: => Boolean)(action: => Unit): Unit =
    while (cond) action

  // description only
  val task: Par[Int] = unit(7)
  val taskSleeping: Par[Int] = sleep(task, 3000)
  val taskSleepingForked: Par[Int] = detach(taskSleeping)

  // preparation
  val es: ExecutorService = Executors.newFixedThreadPool(1)

  // place in the heap where we will store our result
  val ref = new AtomicReference[Int]()
  // latch to be sure that we are ready to extract value
  val latch = new CountDownLatch(1)
  // right here we only passed Executor service to our task
  // and got instance of Detached which is aware of ES
  val detached: Something[Int] = taskSleepingForked(es)
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
