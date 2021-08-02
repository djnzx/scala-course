package catsx.c062contra

import cats.implicits.{toContravariantOps, toFunctorOps}

/**
  * we are trying to create composition
  * Int => Double => String
  */
object C070PartialUnification extends App {

  /** first step */
  val f1:  Int    => Double = (x: Int)    => x.toDouble + 0.1
  /** second step */
  val f2:  Double => String = (y: Double) => y.toString + "!"

  /** composition 1a, scala library */
  val f3s1: Int    => String = f2 compose f1
  /** composition 1b, scala library, just explicit syntax */
  val f3s2: Int    => String = a => f2(f1(a))
  /** composition 2, scala library */
  val f3s3: Int    => String = f1 andThen f2

  /** partial unification works fine here:
    * Function1[Int, Double]
    * becomes F1[Double]
    * and we map the outcome by providing f: Double => String
    * and get: F1[String] */
  val f3m:  Int    => String = f1 map f2
  println(f3m(5)) // 5.1!

  /** but doesn't work here, due to left side elimination. */
//  val func4d: Int => String = f2.contramap(f1)

  /** we need to "fix" output and be able manipulating with input
    * let's define another type */
  type <=[B, A] = A => B
  /** and provide compiler another type (implementation is the same!!!) */
  val f2b: String <= Double = f2
  /** but right now, String will be fixed, and we can operate with Double, and prepend our function */
  val f3c: String <= Int = f2b contramap f1
  /** and we can use our composition */
  println(f3c(5)) // 5.1!
}
