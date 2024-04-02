package cats101.kleisli

import cats.data.Kleisli
import cats.implicits.{catsStdInstancesForOption, catsSyntaxOptionId}
import scala.util.Try
import pprint.{pprintln => println}

object Thoughts extends App {
  /** Int => Option[Int] */
  val inc1: Int => Option[Int] = (a: Int) => (a + 1).some
  /** Int => Option[Int] */
  val mul2: Int => Option[Int] = (a: Int) => (a * 2).some
  /** Int => Option[String] */
  val itos: Int => Option[String] = (a: Int) => a.toString.some
  /** String => Option[Int] */
  val stoi: String => Option[Int] = (s: String) => Try(s.toInt).toOption

  // to compose inc1 and inc2 we need to write
  val composed1: Int => Option[Int] = (a: Int) => inc1(a).flatMap(x => mul2(x))
  println(composed1(10)) // (10+1)*2 => 22

  val inc1k: Kleisli[Option, Int, Int] = Kleisli(inc1)
  val mul2k: Kleisli[Option, Int, Int] = Kleisli(mul2)
  val itosk: Kleisli[Option, Int, String] = Kleisli(itos)
  val stoik: Kleisli[Option, String, Int] = Kleisli(stoi)
  // with Kleisli we can write
  val composed2 = inc1k andThen mul2k
  println(composed2(20)) // (20+1)*2 => 42
  val mapped2: Kleisli[Option, Int, Int] = inc1k.map(_ * 100)
  println(mapped2(5)) // (5+1)*6
}
