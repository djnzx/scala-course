package cats101.c113writer

import cats.Monoid
import cats.data.Writer
import cats.implicits.{catsSyntaxApplicativeId, catsSyntaxWriterId}

object C113WriterExercise2 extends App {
  type Logged[A] = Writer[Vector[String], A]

  val w1: Logged[Unit] = Writer[Vector[String], Unit](implicitly[Monoid[Vector[String]]].empty, ())
  val w2: Logged[Int] = w1.map(_ => 1)
  val w3: Logged[Int] = w2.mapWritten(v => v.appended("Line: A"))
  val w4: Logged[Int] = w3.mapWritten(v => v.appended("Line: B"))
  val w5: Logged[Unit] = Vector("Line: C").tell
  val w7: Logged[Int] = 42.pure[Logged]
  val sv: Vector[String] = 1 to 10 map { n => s"line: $n!" } toVector
  val w9: Logged[Unit] = sv.tell

  /**
    * .tell and .pure are just implicit syntax
    *
    * we can use the value
    * a <- w2
    * we can ignore the value
    * _ <- w2
    *
    * all the Vector[String] will be collected
    *
    * we cam map the value w.map(val => ...)
    * we cam map the log   w.mapWritten(log => ...)
    * we can map both      w.mapBoot((log, val) => ...)
    * we can map both      w.bimap(log => ..., val => ...)
    */
  val wX = for {
    _ <- w1 // empty/unit
    a <- w2 // empty + value = 1
    x <- w4 // A + B + value = 1
    _ <- w5 // C
    _ <- Vector("Line: D").tell
    _ <- Vector("Line: E").tell
    _ <- w9 // line 1..line 10
    b <- w7.map(_ + 1000) // empty + value = 1042
  } yield a + b + x

  // tell(x) = mapWritten(_ + x)
  val wY: Logged[Int] = wX.tell(Vector("Finish¡"))

  val (log: Vector[String], value: Int) = wY.run
  val (log1, value1) = wX.run
  val (log2, value2): (Vector[String], Int) = wX.run
  val (log3, value3): Tuple2[Vector[String], Int] = wX.run

  println(s"log = $log")
  println(s"val = $value")
}
