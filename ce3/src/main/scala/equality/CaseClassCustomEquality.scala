package equality

import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Gen}
import org.scalactic.Equality
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import shapeless.::
import shapeless.Generic
import shapeless.Generic.Aux
import shapeless.HList
import shapeless.HNil

import scala.util.Random

// case class custom equality
object CaseClassCustomEquality {

  trait EqualityST[A] extends Equality[A] {
    def areEqual0(a: A, b: A): Boolean
    def areEqual(a: A, b: Any): Boolean = b match {
      case a2: A => areEqual0(a, a2)
      case _     => false
    }
  }

  implicit def caseClassEq[A, L <: HList](
      implicit gen: Generic.Aux[A, L],
      eqHL: EqualityST[L]
    ): EqualityST[A] = (a: A, b: A) => eqHL.areEqual0(gen.to(a), gen.to(b))

  implicit val hnilEq: EqualityST[HNil] = (_: HNil, _: HNil) => true

  implicit def hconsEq[H, T <: HList](
      implicit
      hEq: EqualityST[H],
      tEq: EqualityST[T]
    ): EqualityST[H :: T] =
    (a: H :: T, b: H :: T) => hEq.areEqual0(a.head, b.head) && tEq.areEqual0(a.tail, b.tail)

  implicit val intEquality: EqualityST[Int]    = (a: Int, b: Int) => math.abs(a - b) < 10
  implicit val strEquality: EqualityST[String] = (a: String, b: String) => a == b
}

class CaseClassCustomEqualitySpec extends AnyFunSpec with Matchers {
  import CaseClassCustomEquality._

  case class Person(name: String, age: Int)
  case class Person1(name: String)

  describe("int primitive") {

    it("int == ") {
      1 shouldEqual 10
    }

    it("int !=") {
      1 should not equal 11
    }

  }

  describe("case class") {

    it("case class ==") {
      val p1 = Person("Jim", 33)
      val p2 = p1.copy(age = p1.age + 1)
      p1 shouldEqual p2
    }

    it("case class !=") {
      val p1 = Person("Jim", 33)
      val p2 = p1.copy(age = p1.age + 10)
      p1 should not equal p2
    }

  }

  describe("equality playground") {

    it("playground1") {
      val a = Generic.apply[(Int, Double)]
      val g = Generic.apply[Person]
      val c = g.to(Person("alex", 33))

      def mkGenT[A: Generic] = Generic[A]

      def mkGenI[A, L <: HList](
          a: A
        )(
          implicit gen: Generic.Aux[A, L]
        ) = Generic[A]

      val g1 = mkGenI(Person("alex", 33))
      val g2 = mkGenT[Person]
    }

  }

  describe("props") {

    implicit val arbitraryPerson: Arbitrary[Person] = Arbitrary(
      for {
        name <- Gen.alphaStr
        age <- Gen.choose(0, 100)
      } yield Person(name, age)
    )

    it("1") {
      val g: Aux[Person, String :: Int :: HNil] = Generic[Person]


      val prop = forAll { (p1: Person) =>
        val p2 = p1.copy(age = p1.age + Random.nextInt(9))
        caseClassEq[Person, String :: Int :: HNil].areEqual(p1, p2)
      }

      prop.check()
    }
  }

}
