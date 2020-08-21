package monoid

import cats.implicits.catsKernelStdGroupForInt
import cats.{Monoid, Semigroup}

/**
  * Magma     : binary op
  * Semigroup : + associative law
  * Monoid    : + identity element
  * Group     : + inversion op
  */
object Theory extends App {
  /**
    * Magma is a category:
    *
    * - type: A
    * - operation `combine` (A, A) => A
    */
  trait Magma[A] {
    def combine(a: A, b: A): A
  }
  /**
    * Semigroup is a category: (Magma + assoc law)
    *
    * - type: A
    * - operation `combine` (A, A) => A
    *
    * - law(s)
    *   - associative: (a * b) * c === a * (b * c)
    */
  val si: Semigroup[Int] = Semigroup[Int]
  /** in Cats operation is defined as addition. Why ??? */
  val s3: Int = si.combine(1,2) // 3
  pprint.pprintln(s3)    // 3

  /**
    * Monoid is a category (Semigroup + identity)
    *
    * - type: A
    * - operation `combine` (A, A) => A
    * - identity element Id: A
    *     - combine(A, Id) = A
    *     - combine(ID, A) = A
    *
    * - law(s)
    *   - associative: (a * b) * c === a * (b * c)
    */
  val mi_add: Monoid[Int] = Monoid[Int]
  val empty: Int = mi_add.empty
  /** in Cats operation is defined as addition. Why ??? */
  val m5: Int = mi_add.combine(3,2)
  pprint.pprintln(empty) // 0
  pprint.pprintln(m5)    // 5
  /** but we can define monoid for Integer multiplication */
  val mi_mult: Monoid[Int] = new Monoid[Int] {
    override def empty: Int = 1
    override def combine(x: Int, y: Int): Int = x * y
  }
  val mempty: Int = mi_mult.empty // 1
  /** in Cats operation is defined as addition. Why ??? */
  val m6: Int = mi_mult.combine(3,2) // 6
  pprint.pprintln(mempty) // 1
  pprint.pprintln(m6)    // 6
  /**
    * Group is a category (Monoid + inversion operation)
    */
}
