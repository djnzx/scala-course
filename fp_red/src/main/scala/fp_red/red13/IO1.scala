package fp_red.red13

import scala.io.StdIn

/**
  * next version of IO which can represent user's input,
  * and can be composed via flatMap/map.
  *
  * now it has form a Monad
  *
  * It still has a problems:
  *
  * - StackOverflow;
  * - A value of type IO[A] is completely opaque (it’s too general)
  */
object IO1 extends App {
  sealed trait IO[A] { self =>
    def run: A
    def map[B]   (f: A => B    ): IO[B] = new IO[B] { def run = f(self.run) }
    def flatMap[B](f: A => IO[B]): IO[B] = new IO[B] { def run = f(self.run).run }
  }

  object IO extends Monad[IO] {
    def unit[A](a: => A): IO[A] = new IO[A] { def run = a }
    def flatMap[A,B](fa: IO[A])(f: A => IO[B]) = fa flatMap f
    def apply[A](a: => A): IO[A] = unit(a) // syntax for IO { .. }

    def ref[A](a: A): IO[IORef[A]] = IO { new IORef(a) }

    /** mutable state management, already wrapped into IO */
    sealed class IORef[A](var value: A) {
      def set(a: A): IO[A] = IO { value = a; a }
      def get: IO[A] = IO { value }
      def modify(f: A => A): IO[A] = get flatMap (a => set(f(a)))
    }
  }

  def readLine: IO[String] = IO { StdIn.readLine }
  def printLine(msg: String): IO[Unit] = IO { println(msg) }
  import IO0.fahrenheitToCelsius

  def converter: IO[Unit] = for {
    _ <- printLine("Enter a temperature in degrees Fahrenheit: ")
    d <- readLine.map(_.toDouble)
    c = fahrenheitToCelsius(d)
    s = c.toString
    _ <- printLine(s)
  } yield ()
  /** to run this: */
  //converter.run

  import IO._

  val echo: IO[Unit] = readLine.flatMap(printLine)
  val readInt: IO[Int] = readLine.map(_.toInt)
  val readInts: IO[(Int,Int)] = readInt ** readInt
  val fivePrompts: IO[Unit] = replicateM_(5)(converter)
  val lines: IO[List[String]] = replicateM(10)(readLine)

  /**
    * Larger example using various monadic combinators
    */
  val helpString = """
                     | The Amazing Factorial REPL, v2.0
                     | q - quit
                     | <number> - compute the factorial of the given number
                     | <anything else> - bomb with horrible error
  """.trim.stripMargin

  def factorial(n: Int): IO[Int] = for {
    acc <- ref(1)
    //               stream 1,2,...n  f: A => IO[Unit]. do the job and return unit
    _   <- foreachM (1 to n toStream) (i => acc.modify(x => x * i).skip)
    res <- acc.get
  } yield res

  val factorialREPL: IO[Unit] =
    printLine(helpString) *>
      doWhile { readLine } { line =>
        when (line != "q") { for {
          i <- IO { line.toInt }
          n <- factorial(i)
          m = s"factorial of $i is equal to $n"
          _ <- printLine(m)
        } yield () }
      }
      
  factorialREPL.run
}
