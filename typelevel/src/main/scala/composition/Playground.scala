package composition

import cats.{Monoid, Semigroup}
import cats.implicits.catsKernelStdGroupForInt

object Playground {
  /**
    * it has:
    * - type
    * - operation (op)
    * it has identity: 
    *  - A op Id = A
    *  - ID op A = A
    * it has combine:
    * f(A, A) => A
    */
  val mi: Monoid[Int] = Monoid[Int]
  val empty: Int = mi.empty
  val combined: Int = mi.combine(1,2)
  
  val si: Semigroup[Int] = Semigroup[Int]
  si.combine(1,2)
}
