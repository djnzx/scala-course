package tools.spec

import org.scalatest.BeforeAndAfterAll
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

abstract class ASpec extends AnyFunSpec with Matchers with BeforeAndAfterAll {

  def println[A](a: A): Unit = pprint.pprintln(a)

  def runAll[A, B](data: Seq[(A, B)], f: A => B, debug: Boolean = false): Unit =
    for {
      (in, out) <- data
    } {
      if (debug) Predef.println(s"Going to check '${Console.MAGENTA}$in${Console.RESET}'")
      f(in) shouldEqual out
    }

  def runAllS[A, B](data: Seq[(A, B)], fs: Seq[A => B], debug: Boolean = false): Unit =
    fs.foreach(f => runAll(data, f, debug))

  def runAllD[A, B](data: Seq[(A, B)], f: A => B): Unit = runAll(data, f, debug = true)

  def runAllSD[A, B](data: Seq[(A, B)], fs: Seq[A => B]): Unit =
    fs.foreach(f => runAll(data, f, debug = true))

}
