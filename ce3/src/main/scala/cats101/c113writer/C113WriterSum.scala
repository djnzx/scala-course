package cats101.c113writer

import cats.Monoid
import cats.data.Writer

object C113WriterSum extends App {
  type Logged[A] = Writer[Vector[String], Int]

  // empty writer with empty Vector[String] and empty integer (zero)
  val zero: Logged[Int] = Writer[Vector[String], Int](
    implicitly[Monoid[Vector[String]]].empty,
    implicitly[Monoid[Int]].empty
  )

  // function takes writer and number, and returns the new writer with the new data
  val addAndLog: (Logged[Int], Int) => Logged[Int] =
    (w: Logged[Int], n: Int) => w.mapBoth((log, acc) => {
      val newAcc = acc + n
      val newLog = log.appended(s"$acc + $n = $newAcc")
      (newLog, newAcc)
    })

  // initial data
  val data: List[Int] = 1 to 5 toList

  // folding with logging
  val folded: Logged[Int] = data.foldLeft(zero)(addAndLog)

  // extracting
  val (a, b) = folded.run
  // printing
  println(a.mkString("\n"))
  println(b)
}
