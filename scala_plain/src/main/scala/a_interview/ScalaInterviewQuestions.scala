package a_interview

import scala.collection.mutable.ArrayBuffer

object ScalaInterviewQuestions extends App {

  val f1 = (1 to 10).filter(_%2==0)
  val f2 = (1 to 10).flatMap {
    case x if x%2 == 0 => Some(x)
    case _ => None
  }
  println(f1)
  println(f2)

  object problem_01 {

    def switch(s: String) = {
      if (s == "red") {
        ???
      }
      else if (s == "yellow") {
        ???
      }
      else if (s == "green") {
        ???
      }
    }

  }

  object problem_01_solution {

    Option(5).map(_ + 5)
    val _ = 5 + 5

    val plus2: Int => Int = _ + 2

    sealed trait Color { def s }
    case object Red extends Color { val s  = "red" }
    case object Yellow extends Color { val s  = "yellow" }
    case object Green extends Color { val s = "green"}

    def switch(color: Color) = color match {
      case Red => ???
      case Yellow => ???
      case Green => ???
      case _ => ???
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

    def abc1(a: Int): String = ???
    val abc2: Int => String = ???
    def abc3[A, B](a: A): B = ???
    val abc4 = abc3[Int, String] _ // -

    // 3
    val x1 = List(1,2,3)
    val x2 = ArrayBuffer(1,2,3)
    var x3 = List(1,2,3)
    var x4 = ArrayBuffer(1,2,3)

    // 4. monad

    def go[A <: String, B >: Int](a: A): Int = { // -
      a.length
    }

    val x: Seq[(Int, String)] = for {
      a <- List(1,2,3)
      if a > 1
      b <- List("a","b")
                  } yield (a, b)

    object Even {
      def unapply(x: Int): Option[String] = ???
    }
    object Odd {
      def unapply(x: Int): Option[String] = ???
    }

    def go(x: Int): String = x match {
      case Even(msg) => msg
      case Odd(msg) => msg
    }

    go(4) // 4 is even
    go(5) // 5 is odd

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
