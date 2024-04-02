package cats101.applicativex

import cats.{Applicative, Semigroupal}
import cats.data.Validated
import cats.implicits.{catsKernelStdMonoidForList, catsStdInstancesForList}

/**
  * typical use case is writing validation logic 
  */
object ApplicativeEx2 extends App{
  val add: Int => Int = (x: Int) => x + 1
  
  val lifted: List[Int] => List[Int] = Applicative[List].lift(add)

  
  type AllErrorsOr[A] = Validated[List[String], A]
  
  /**
    * Cartesian renamed to Semigroupal in 2017
    */
  val r: AllErrorsOr[(Nothing, Nothing)] =
    Semigroupal[AllErrorsOr].product(
      Validated.invalid(List("Error1")),
      Validated.invalid(List("Error2"))
    )
  println(r)
}
