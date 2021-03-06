package fp_red.red15

import fp_red.red13.IO

object Playground0 extends App {

  import SimpleStreamTransducers.Process
  import SimpleStreamTransducers.Process._

  def processFile[A,B](f: java.io.File,
                       p: Process[String, A],
                       z: B)(g: (B, A) => B): IO[B] = IO {
    @annotation.tailrec
    def go(ss: Iterator[String], cur: Process[String, A], acc: B): B =
      cur match {
        case Halt() => acc
        case Await(recv) =>
          val next = if (ss.hasNext) recv(Some(ss.next()))
          else recv(None)
          go(ss, next, acc)
        case Emit(h, t) => go(ss, t, g(acc, h))
      }
    val s = scala.io.Source.fromFile(f)
    try go(s.getLines(), p, z)
    finally s.close
  }

  /**
    * Exercise 9: Write a program that reads degrees fahrenheit as `Double` values from a file,
    * converts each temperature to celsius, and writes results to another file.
    */

  // This process defines the here is core logic, a transducer that converts input lines
  // (assumed to be temperatures in degrees fahrenheit) to output lines (temperatures in
  // degrees celsius). Left as an exercise to supply another wrapper like `processFile`
  // to actually do the IO and drive the process.
  def convertFahrenheit: Process[String,String] =
    filter((line: String) => !line.startsWith("#")) |>
      filter(line => line.trim.nonEmpty) |>
      lift(line => toCelsius(line.toDouble).toString)

  def toCelsius(fahrenheit: Double): Double =
    (5.0 / 9.0) * (fahrenheit - 32.0)

}
