package rockthejvm.tl

object TypeLevelProg {

  import scala.reflect.runtime.universe._
  def show[T](value: T)(implicit tag: TypeTag[T]) = tag.toString().replace("rockthejvm.tl.TypeLevelProg.", "")

  // Peano arithmetic
  trait Nat
  class _0 extends Nat
  class Succ[N <: Nat] extends Nat
  
  type _1 = Succ[_0]
  type _2 = Succ[_1] // Succ[Succ[_0]]
  type _3 = Succ[_2]
  type _4 = Succ[_3]
  type _5 = Succ[_4]
  
  // error - must extend Nat
  //type Animal = Succ[Int]
  
  // we want to check less for types
  trait <[A <: Nat, B <: Nat]
  object < {
    implicit def ltBasic[B <: Nat]: <[_0, Succ[B]] = new <[_0, Succ[B]] {}
    implicit def inductive[A <: Nat, B <: Nat](implicit lt: <[A, B]): <[Succ[A], Succ[B]] = new <[Succ[A], Succ[B]] {}
    def apply[A <: Nat, B <: Nat](implicit lt: <[A, B]) = lt
  }
  // that means that compiler can infer that _0 < _1
  val comparison: <[_0, _1] = <[_0, _1]
  val comparison2: _1 < _3 = <[_1, _3]
//  val invalid: _3 < _2 = <[_3, _2] // will not compile

  trait <=[A <: Nat, B <: Nat]
  object <= {
    implicit def lteBasic[B <: Nat]: <=[_0, B] = new <=[_0, B] {}
    implicit def inductive[A <: Nat, B <: Nat](implicit lte: <=[A, B]): <=[Succ[A], Succ[B]] = new <=[Succ[A], Succ[B]] {}
    def apply[A <: Nat, B <: Nat](implicit lte: <=[A, B]) = lte
  }

  val comparison3: _1 <= _1 = <=[_1, _1]
//  val comparisonInvalid: _2 <= _1 = <=[_2, _1] // will not compile

  // ADD NUMBERS as a type
  trait +[A <: Nat, B <: Nat] {
    type Result <: Nat
  }
  object + {
    type Plus[A <: Nat, B <: Nat, S <: Nat] = +[A, B] { type Result = S }
    // 0 + 0 = 0
    implicit val zero: Plus[_0, _0, _0] = new +[_0, _0] { type Result = _0 }
    // 0 + A = A
    implicit def basicRight[A <: Nat](implicit lt: _0 < A): Plus[_0, A, A] = new +[_0, A] { type Result = A }
    // A + 0 = A
    implicit def basicLeft[A <: Nat](implicit lt: _0 < A): Plus[A, _0, A] = new +[A, _0] { type Result = A }
//     A + B = S => Succ[A] + Succ[B] = Succ[Succ[S]]
    implicit def inductive[A <: Nat, B <: Nat, S <: Nat](implicit plus: Plus[A, B, S]): Plus[Succ[A], Succ[B], Succ[Succ[S]]] = 
      new +[Succ[A], Succ[B]] { type Result = Succ[Succ[S]]}
    def apply[A <: Nat, B <: Nat](implicit plus: +[A, B]): Plus[A, B, plus.Result] = plus
  }
  
  // OK, intelliJ Bug
  val zero: +[_0, _0] = +.apply
  val two: +[_0, _2] = +.apply
  val three: +[_3, _0] = +.apply
  val four: +[_1, _3] = +.apply
//  val inv: +[_1, _2, _4] = +.apply // will not compile !!!
  
  
  def main(args: Array[String]): Unit = {
//    pprint.pprintln(show(comparison2))
//    pprint.pprintln(show(zero))
//    pprint.pprintln(show(two))
//    pprint.pprintln(show(three))
    pprint.pprintln(show(+.apply[_1, _3]))
  }
  
}
