package hackerrankfp.d230410

import hackerrankfp.util.Console
import hackerrankfp.util.Console.Real
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

// https://www.hackerrank.com/challenges/expressions/problem
object Expression {

  sealed abstract class Op {

    def show: String = this match {
      case Op.Add => "+"
      case Op.Sub => "-"
      case Op.Mul => "*"
    }

    def apply(a: BigInt, x: Int): BigInt = this match {
      case Op.Add => a + x
      case Op.Sub => a - x
      case Op.Mul => a * x
    }

  }
  object Op {
    case object Add extends Op
    case object Sub extends Op
    case object Mul extends Op
    val all  = List(Mul, Add, Sub)
    val allL = LazyList(Mul, Add, Sub)
  }

  def isValid(x: BigInt): Boolean = x % 101 == 0

  def show(ns: List[Int], ops: List[Op]): String = {
    def go(xs: List[Int], os: List[Op], acc: List[String]): List[String] = (xs, os) match {
      case (Nil, Nil)         => acc.reverse
      case (x :: xs, o :: os) => go(xs, os, x.toString :: o.show :: acc)
      case _                  => sys.error("`ops` is designed to be one number less than `ns`")
    }
    go(ns.tail, ops, ns.head.toString :: Nil).mkString
  }

  def combinationsR(xa: BigInt, ns: List[Int]): LazyList[(BigInt, List[Op])] = ns match {
      case Nil     => LazyList.empty
      case x :: xs =>
        Op.allL.flatMap { op =>
          val xa2 = op(xa, x)
          val tail = combinationsR(xa2, xs)
          if (tail.isEmpty) LazyList(xa2 -> List(op))
          else tail.map { case (a, ops) => a -> (op :: ops) }
        }
    }

  def solveIt(xs: List[Int]) =
    combinationsR(xs.head, xs.tail)
      .filter { case (n, _) => isValid(n) }
      .map { case (n, ops) => show(xs, ops) }

  def doSolve(console: Console) = {
    val _  = console.readLine()
    val xs = console.readLine().split(" ").map(_.toInt).toList
//    val s  =
    solveIt(xs)
      .foreach(println)
//      .headOption.getOrElse("*0*")
//    println(s)
  }

  def main(xs: Array[String]): Unit = doSolve(Real)

}

object PlainImplementation {

  import Expression._
  def variations(n: Int): List[List[Op]] = {
    def go(n: Int, outcome: List[List[Op]]): List[List[Op]] = n match {
      case 0 => outcome
      case n => go(n - 1, outcome.flatMap(ops => Op.all.map(_ :: ops)))
    }
    go(n, List(Nil))
  }

  def find(numbers: List[Int]) =
    variations(numbers.length - 1)
      .map { ops =>
        val r = (ops zip numbers.tail).foldLeft(BigInt(numbers.head)) { case (a, (op, x)) => op(a, x) }
        r -> ops
      }
      .filter { case (r, _) => isValid(r) }
      .map { case (r, ops) => show(numbers, ops) + s" = $r" }

}

object TailRecursiveButEager {
  import Expression._

  def combinationsTR(acc: List[(BigInt, List[Op])], xs: List[Int]): List[(BigInt, List[Op])] =
    xs match {
      case Nil     => acc.map { case (a, ops) => a -> ops.reverse }
      case x :: xs =>
        val next = acc.flatMap { case (a, ops) => Op.all.map(op => op(a, x) -> (op :: ops)) }
        combinationsTR(next, xs)
    }

}

class ExpressionSpec extends AnyFunSuite with Matchers {

  import Expression.Op._
  import Expression._
  import PlainImplementation._

  test("show") {
    val r = show(
      List(1, 2, 3, 4, 5),
      List(Add, Sub, Mul, Sub)
    )
    pprint.pprintln(r)
    r shouldBe "1+2-3*4-5"
  }

  test("variations 1") {
    variations(1).toSet shouldBe Set(List(Add), List(Sub), List(Mul))
  }

  test("variations 2") {
    variations(2).toSet shouldBe Set(
      List(Add, Add),
      List(Sub, Add),
      List(Mul, Add),
      List(Add, Sub),
      List(Sub, Sub),
      List(Mul, Sub),
      List(Add, Mul),
      List(Sub, Mul),
      List(Mul, Mul)
    )
  }

  val numbers = List[Int](55, 3, 45, 33, 25)

  // 55+3-45*33-25 = 404
  // 55*3+45-33+25 = 202
  test("solve cached 1") {
    solveIt(numbers)
      .foreach(println)
  }

  test("solve cached 0") {
    val numbers = List[Int](22, 79, 21)
    solveIt(numbers)
      .foreach(println)
  }

  test("solve cached 3") {
    val xs = "59 34 36 63 79 82 20 4 81 16 30 93 50 38 78 10 22 61 91 27 18 78 96 19 38 10 3 17 42 90 98 60 1 63 16 28 97 45 19 35 44 56 77 43 24 42 28 35 95 44 61 55 32 84"
      .split(" ")
      .map(_.toInt)
      .toList

    println(xs.size)
    solveIt(xs)
//      .foreach(println)
  }

  // stack overflow
  test("solve cached 4") {
    val xs =
      "1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 100"
        .split(" ")
        .map(_.toInt)
        .toList

    println(xs.size)
    solveIt(xs)
      .take(10)
      .foreach(println)
  }

  // 55*3+45-33+25 = 202
  // 55+3-45*33-25 = 404
  test("!") {
    find(numbers)
      .foreach(println)
  }

}
