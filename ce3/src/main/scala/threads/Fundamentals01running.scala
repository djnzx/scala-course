package threads

import cats.effect.IO
import munit.CatsEffectSuite
import org.scalatest.matchers.should.Matchers

class Fundamentals01running extends CatsEffectSuite with Tools with Matchers {

  test("Basic Color Syntax") {
    println(1.red)
    println(2.green)
    println(3.blue)
    println(4.cyan)
    println(5.magenta)
    println(6.yellow)
    println(List(1, 2, 3).colored)
  }

  test("IO, IO.delay(x) runs on compute, apply = delay") {
    IO.delay(tn)
      .flatTap(assertComputeF)
      .flatTap(printlnF)
  }

  test("IO.pure(x) runs on compute") {
    IO.pure(tn)
      .flatTap(assertComputeF)
      .flatTap(printlnF)
  }

  test("IO.blocking(x) runs on blocker") {
    IO.blocking(tn)
      .flatTap(assertBlockerF)
      .flatTap(printlnF)
  }

  test("IO.interruptible(x) runs on blocker") {
    IO.interruptible(tn)
      .flatTap(assertBlockerF)
      .flatTap(printlnF)
  }

  test("IO.interruptibleMany(x) runs on blocker") {
    IO.interruptibleMany(tn)
      .flatTap(assertBlockerF)
      .flatTap(printlnF)
  }

}
