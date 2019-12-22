package _degoes.tips12

import x00lessons.variance.VarianceApp2.{Animal, Dog}

object Tip03EmbraceVariants extends App {

  /**
    * Java doesn't have declaration site variants, Scala does!
    * https://docs.scala-lang.org/tour/variances.html
    *
    * class Foo[+A] // A covariant class
    * class Bar[-A] // A contravariant class
    * class Baz[A]  // An invariant class
    */
  // invariance
  sealed trait CacheValueINV[A]
  object CacheValueINV {
    final case class Cached[A](origin: A) extends CacheValueINV[A]
    final case class UnCached[A](origin: A) extends CacheValueINV[A]
  }
  // covariance, subtyping allowed
  sealed trait CacheValueCOV[+A]
  object CacheValueCOV {
    final case class Cached[+A](origin: A) extends CacheValueCOV[A]
    final case object UnCached extends CacheValueCOV[Nothing]
  }

  def feed1(what: CacheValueINV[Animal]) = ???
  def feed2(what: CacheValueCOV[Animal]) = ???
  val dogINV = CacheValueINV.Cached(Dog("Jack"))
  val dogCOV = CacheValueCOV.Cached(Dog("Jack"))
//   doesn't work because of invariance. to make it work - we must `sealed trait CacheValue[+A]`
//  feed1(dogINV)
  feed2(dogCOV)

}
