package tl

object TypeLevel1 {

  import scala.reflect.runtime.universe._
  def show[A](a: A)(implicit tag: TypeTag[A]) = tag.toString().replace("tl.TypeLevel1.", "")

  trait Nat
  class _0 extends Nat
  class Succ[N <: Nat] extends Nat

  type _1 = Succ[_0]
  type _2 = Succ[_1] // Succ[Succ[_0]]
  type _3 = Succ[_2]
  type _4 = Succ[_3]
  type _5 = Succ[_4]

  // _2 < _4

  trait <[A <: Nat, B <: Nat]
  object < {
    implicit def ltBasic[B <: Nat]: <[_0, Succ[B]] = new <[_0, Succ[B]] {}
    implicit def inductive[A <: Nat, B <: Nat](implicit lt: <[A, B]): <[Succ[A], Succ[B]] = new <[Succ[A], Succ[B]] {}

    def apply[A <: Nat, B <: Nat](implicit lt: <[A, B]) = lt
  }

  trait <=[A <: Nat, B <: Nat]
  object <= {
    implicit def lteBasic[B <: Nat]: <=[_0, B] = new <=[_0, B] {}
    implicit def inductive[A <: Nat, B <: Nat](implicit lte: <=[A, B]): Succ[A] <= Succ[B] = new <=[Succ[A], Succ[B]] {}

    def apply[A <: Nat, B <: Nat](implicit lte: <=[A, B]) = lte
  }

  val comparison1: _0 < _1 = <[_0, _1]
  val comparison2: _1 < _3 = <[_1, _3]
//  val comparison3: _3 < _2 = <[_3, _2]
  val comparison4: _1 < _4 = <[_1, _4]

  val comparison5: _1 <= _1 = <=[_1, _1]
//  val comparison6: _2 <= _1 = <=[_2, _1]

  def main(xs: Array[String]): Unit = {
    println(show(comparison4))
  }

}
