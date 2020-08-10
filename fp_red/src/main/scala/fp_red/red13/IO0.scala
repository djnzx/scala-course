package fp_red.red13

import scala.io.StdIn

/**
  * minimal version of IO which is able to represent output and composability.
  *
  * It's a Monoid. it has `empty` and `++` operation.
  */
object IO0 {
  trait IO { self =>
    def run: Unit
    def ++(io: IO): IO = new IO {
      def run = { self.run; io.run }
    }
  }
  object IO {
    def empty: IO = new IO { def run = () }
  }

  def fahrenheitToCelsius(f: Double): Double = (f - 32) * 5.0/9.0

  /**
    * but, we still can't represent input
    */
  def converter: Unit = {
    println("Enter a temperature in degrees Fahrenheit: ")
    val d = StdIn.readLine.toDouble
    println(fahrenheitToCelsius(d))
  }
}
