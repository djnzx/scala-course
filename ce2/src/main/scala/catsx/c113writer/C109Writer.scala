package catsx.c113writer

import cats.Id
import cats.data.WriterT

/**
  * type Writer[W, A] = WriterT[Id, W, A]
  */
object C109Writer extends App {

  import cats.data.Writer

  // log and result #1
  //  val wVL0: Writer[Vector[String], Int] = implicitly[Writer[Vector[String], Int]]
  val wVL1: Writer[Vector[String], Int] = Writer(Vector("Line 1", "Line 2"), 1859)
  val wVL3: Writer[Vector[String], Int] = Writer.apply[Vector[String], Int](Vector("Line 1", "Line 2"), 1859)

  // extract value only
  val v1: Id[Int] = wVL3.value
  val v2: Int = wVL3.value
  // extract log only
  val w1: Id[Vector[String]] = wVL3.written
  val w2: Vector[String] = wVL3.written
  // extract both
  val r: (Vector[String], Int) = wVL1.run

  type Logged[A] = Writer[Vector[String], A]

  // result only

  import cats.instances.vector._
  import cats.syntax.applicative._ // pure

  val wV1: Logged[Int] = 123.pure[Logged]
  val wV2: Writer[Vector[String], Int] = 123.pure[Logged]

  // log only

  import cats.syntax.writer._ // tell

  val wL1: Writer[Vector[String], Unit] = Vector("line A", "line B", "line C").tell
  // log and result #2
  val wVL2: Writer[Vector[String], Int] = 123.writer(Vector("log1", "log2", "log3"))

  val wCombined = for {
    a <- 10.pure[Logged]
    _ <- Vector("la", "lb", "lc").tell
    b <- 32.writer(Vector("ld", "le"))
  } yield a + b
  // map log only
  val wMapped = wCombined.mapWritten(v => v.map(s => s.toUpperCase))
  // map both, syntax 1
  val wMapped2 = wMapped.mapBoth((l, v) => (l.map(_.toLowerCase.capitalize), v + 100))
  // map both, syntax 2
  val wMapped2a = wMapped.bimap(l => l.map(_.toLowerCase.capitalize), v => v + 100)
  // map value only
  val wMapped3 = wMapped2.map(_ + 1000)
  // clear log
  val wWoLog = wMapped3.reset
  // swap the log and the value
  val swapped: WriterT[Id, Int, Vector[String]] = wMapped3.swap
  val (log, value) = wMapped3.run
  println(log)
  println(value)

}
