package fs2x

import cats.implicits._

// https://stackoverflow.com/questions/76805883/when-the-scala-code-in-currying-functions-is-evaluated-and-why
object SO202308022 extends App {

  def logAndGet[A](t: String)(a: => A): A = {
    println(s"log: $t")
    a
  }

  def publish(x: Double): Unit = println(s"publishing $x")

  val maybe = none[Double]

  val unit = ()

  val a = maybe.fold(
    logAndGet("none")(unit)
  ) {
    logAndGet("some")(x => publish(x))
  }

  println("---")

  val c = maybe.fold(
    logAndGet("none")(unit)
  )(
    x => logAndGet("some")(publish(x))
  )


}
