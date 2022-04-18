package catsx.c113writer

import cats.data.Writer
import cats.implicits.{catsSyntaxApplicativeId, catsSyntaxWriterId}

object C113WriterApp0 extends App {

  val w: Writer[Vector[String], Int] = Writer(
    Vector(
      "It was the best of times",
      "it was the worst of times"
    ),
    1859
  )

  type Logged[A] = Writer[Vector[String], A]
  val w0: Writer[Vector[String], Int] = 123.pure[Logged] // lift value
  val w1: Writer[Vector[String], Unit] = Vector("msg1", "msg2", "msg3").tell // lift text to the context
  val w2: Writer[Vector[String], Int] = w1.map(_ => 1) // map values
  val w3: Writer[Vector[Int], Int] = w2.mapWritten(_.map(_.length)) // map written part
  // run written
  val wr: Vector[Int] = w3.written
  // run value
  val va: Int = w3.value

  // run transformations
  val (wr2, vl2): (Vector[Int], Int) = w3.run

  /** The log in a Writer is preserved when we map
    * or combined when flatMap over it */
  for {
    w <- w0
    wx: Int = w
  } yield ()

  w.bimap(???,???)
  w.mapBoth(???)



}
