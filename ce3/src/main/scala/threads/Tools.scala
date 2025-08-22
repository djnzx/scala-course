package threads

import cats.effect.IO
import org.scalatest.Assertion
import org.scalatest.matchers.should.Matchers

trait Tools { _: Matchers =>

  /** thread name */
  def tn = Thread.currentThread().getName
  def tnF: IO[String] = IO.delay(tn)

  /** plain print */
  def printF(x: Any): IO[Unit] = IO(print(x))
  def printlnF(x: Any): IO[Unit] = IO(println(x))

  implicit class AnyOps(x: Any) {

    def red = fansi.Color.Red(x.toString)
    def green = fansi.Color.Green(x.toString)
    def blue = fansi.Color.Blue(x.toString)

    def cyan = fansi.Color.Cyan(x.toString)
    def magenta = fansi.Color.Magenta(x.toString)
    def yellow = fansi.Color.Yellow(x.toString)

    def colored: String = pprint.PPrinter.Color.apply(x).render

  }

  /** colored print */
  def pprintF(x: Any): IO[Unit] = IO(print(x.colored))
  def pprintlnF(x: Any): IO[Unit] = IO(println(x.colored))

  def assertComputeF(tn: String): IO[Assertion] = IO {
    tn.startsWith("io-compute") shouldBe true
    tn.startsWith("io-compute-blocker") shouldBe false
  }

  def assertBlockerF(tn: String): IO[Assertion] = IO {
    tn.startsWith("io-compute-blocker") shouldBe true
  }

//  implicit class DebugOps[A](fa: IO[A]) {
//    def d: IO[A] = fa.flatTap(a => printF(s"$a:"))
//    def dtF: IO[A] = d.flatTap(_ => printF(tn))
//  }

}
