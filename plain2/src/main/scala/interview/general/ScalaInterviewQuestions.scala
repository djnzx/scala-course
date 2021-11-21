package interview.general

import scala.collection.mutable.ArrayBuffer

object ScalaInterviewQuestions extends App {

  /**   1. val / lazy val / def
    */
  object problem_01 {

    def switch(s: String) = {
      if (s == "red") {
        ???
      } else if (s == "yellow") {
        ???
      } else if (s == "green") {
        ???
      }
    }

  }

  /** 2. Nothing / Any / Unit
    */
  object problem_01_solution {

    Option(5).map(_ + 5)
    locally {
      val _ = 5 + 5
    }

    val plus2: Int => Int = _ + 2

    sealed trait Color { def s }
    case object Red extends Color { val s = "red" }
    case object Yellow extends Color { val s = "yellow" }
    case object Green extends Color { val s = "green" }

    def switch(color: Color) = color match {
      case Red    => ???
      case Yellow => ???
      case Green  => ???
      case _      => ???
    }

  }

  def go[A: Ordering](a: A) = ???

  object problems {
    // list. reverse
    // list. filter
    // implement map for filter

    // 1. sealed trait

    // 2. var / val / def / lazy val

    // object / case object

    // partial function

    def abc1(a: Int): String = ???
    val abc2: Int => String = ???
    def abc3[A, B](a: A): B = ???
    val abc4 = abc3[Int, String] _ // -

    // 3
    val x1 = List(1, 2, 3)
    val x2 = ArrayBuffer(1, 2, 3)
    var x3 = List(1, 2, 3)
    var x4 = ArrayBuffer(1, 2, 3)

    // 4. monad

    def go[A <: String, B >: Int](a: A): Int = { // -
      a.length
    }

    val x = for {
      a <- List(1, 2, 3)
      if a > 1
      b <- List("a", "b")
    } yield (a, b)

    // recursive + tail recursive
    def len[A](xs: List[A]): Int = ???

    def reverse[A](xs: List[A]): List[A] = ???
    // implicit parameter
    // implicit conversion
    // implicit syntax

    // SQL
    // what is functional programming
    // Scala collections
    // how List works?
    // practical task: reverse a list (+ O(?), tail recursion)
    // practical task: create a method to divide one int by another int
    // for comprehensions
    // what are implicits? why are they useful? extension methods?
    // type classes (implement a type class, Show for example)
    // type hierarchy. List[Any] vs List[String] - covariant/contravariant types in Scala
    // sealed
    // Cats

    //
    // filter / withFilter
    //
    // type class
  }

}
