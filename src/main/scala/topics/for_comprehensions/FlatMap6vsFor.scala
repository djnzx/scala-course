package topics.for_comprehensions

import scala.util.Random

object FlatMap6vsFor extends App {
  def fInt = Some(123)
  def fString = Some("Hello")
  def fBool = Some(true)

  val x: Option[((Int, String), Boolean)] = for {
    a <- fInt
    b <- fString
    c <- fBool
  } yield a -> b -> c

  // map manipulates with f: A=>B
  val o1: Option[Int] = Some(234).map(v => v + 1) // Option[Int] = Some(235)
  println(o1)

  // set of binders
  val random = (max: Int) => Some(Random.nextInt(max))
  def shift(delta: Int): Int => Some[Int] = (base: Int) => Some(base + delta)
  val noneg = (x: Int) => if (x<0) None else Some(x)
  val mult2 = (x: Int) => Some(x * 2)
  val contrast = (x: Int) => Some(x / 10 * 10)

  // map works with value
  // flatMap acts as a bind in Haskell, it can change the type
  // for example Some -> None
  val base = random(200)
  val chain: Option[Int] = base
    .flatMap(x => { println(s"after random: $x"); Option(x) })
    .flatMap(shift(-100))
    .flatMap(x => { println(s"after shift: $x"); Option(x) })
    .map(_ + 1)
    .flatMap(mult2)
    .flatMap(x => { println(s"after mult2: $x"); Option(x) })
    .flatMap(noneg)
    .flatMap(x => { println(s"after noneg: $x"); Option(x) })
    .flatMap(contrast)
    .flatMap(x => { println(s"after contrast: $x"); Option(x) })

  val finite = chain.getOrElse(-999)
  val finiteFlex = chain.fold(-999)(x => x)

  println(s"finite: $finite")
  println(s"finiteFlex: $finiteFlex")

  // flatMap manipulates with f: A=>B
  val checker:
    Int => Option[Int] = (w: Int) => if (w % 2 == 1) Some(1) else None
  val o2: Option[Int] = Some(234).flatMap(checker) // Option[Int] = Some(233)

}
