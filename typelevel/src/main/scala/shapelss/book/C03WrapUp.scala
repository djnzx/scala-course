package shapelss.book

import shapeless._

object C03WrapUp {

  object model {
    trait MyTC[A]
    implicit def iInstance: MyTC[Int] = ???
    implicit def sInstance: MyTC[String] = ???
    implicit def bInstance: MyTC[Boolean] = ???
  }

  import model._

  /** product */
  implicit def hnilInstance: MyTC[HNil] = ???

  implicit def hlistInstance[H, T <: HList](
      implicit
      hInstance: Lazy[MyTC[H]], // LAZY !
      tInstance: MyTC[T],
    ): MyTC[H :: T] = ???

  /** coproduct */
  implicit def cnilInstance: MyTC[CNil] = ???

  implicit def coproductInstance[H, T <: Coproduct](
      implicit
      hInstance: Lazy[MyTC[H]], // LAZY !
      tInstance: MyTC[T],
    ): MyTC[H :+: T] = ???

  /** generic */
  implicit def genericInstance[A, R](
      implicit
      generic: Generic.Aux[A, R],
      rInstance: Lazy[MyTC[R]], // LAZY !
    ): MyTC[A] = ???
}
