package shapelss.coproduct

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import shapeless._
import shapeless.ops.coproduct.Inject

class WrappingCoproduct extends AnyFunSpec with Matchers {

  object ScalaOutOfTheBox {

    sealed trait R
    case class C1() extends R
    case class C2() extends R
    case object C3 extends R

    def whatever(x: Int): R = x match {
      case 1 => C1()
      case 2 => C2()
      case _ => C3
    }

  }

  final case class C1()
  final case class C2()
  final case object C3

  type R2 = C3.type :+: CNil
//  type R1 = C1 :+: C2 :+: C3.type :+: CNil
  type R1 = :+:[C1,  :+:[C2,  :+:[C3.type, CNil]]]

  object R1 {
    def apply[A](a: A)(implicit inj: Inject[R1, A]): R1 = inj(a)
  }
  object R2 {
    def apply[A](a: A)(implicit inj: Inject[R2, A]): R2 = inj(a)
  }

  def whatever(x: Int): R1 = x match {
    case 1 => R1(C1())
//    case 1 => Coproduct[R](C1())
    case 2 => Coproduct[R1](C2())
    case _ => R1(C3)
//    case _ => Inr(Inr(Inl(C3)))
//    case _ => Coproduct[R](C3)
  }

  it("1") {
    val x = whatever(3)
    x shouldBe Inr(tail = Inr(tail = Inl(head = C3)))
    x shouldBe Inr(Inr(Inl(C3)))
    x shouldBe Coproduct.apply[R1](C3)
    x shouldBe R1(C3)
    x.unify shouldBe C3
    pprint.pprintln(whatever(1)) // Inl(head = C1())
    pprint.pprintln(whatever(2)) // Inr(tail = Inl(head = C2()))
    pprint.pprintln(whatever(3)) // Inr(tail = Inr(tail = Inl(head = C3)))
    pprint.pprintln(x.unify)     // C3
  }

  it("2") {
    val x = C3
    val cp1: R1 = R1(x)
    val cp2: R2 = R2(x)
    pprint.pprintln(cp1) // Inr(tail = Inr(tail = Inl(head = C3)))
    pprint.pprintln(cp2) // Inl(head = C3)
    cp1 should not be cp2
    cp1.unify shouldBe cp2.unify
    cp1.unify shouldBe x
    cp2.unify shouldBe x
  }

}
