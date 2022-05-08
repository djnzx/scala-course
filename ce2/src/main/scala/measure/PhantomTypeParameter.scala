package measure

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.Checkers

object PhantomTypeParameter {

  trait Describe[A] {
    def name: String
  }
  object Describe {
    def apply[A](n: String): Describe[A] = new Describe[A] { val name = n }
  }

  trait MeasureUnit
  trait Length extends MeasureUnit
  trait KM extends Length
  trait MI extends Length
  trait Weight extends MeasureUnit
  trait KG extends Weight
  trait LB extends Weight

  trait DescribeInstances {
    implicit val describeKM: Describe[KM] = Describe[KM]("km")
    implicit val describeMI: Describe[MI] = Describe[MI]("mi")
    implicit val describeKG: Describe[KG] = Describe[KG]("kg")
    implicit val describeLB: Describe[LB] = Describe[LB]("lb")
  }

  final case class Quantity[A: Describe](value: Double) {
    override def toString: String = s"$value ${implicitly[Describe[A]].name}"
  }

  trait QtyConversion[U1, U2] {
    def convert(q1: Quantity[U1])(implicit conv: QtyConversion[U1, U2], d2: Describe[U2]): Quantity[U2]
  }

  final class QtyConversionImpl[U1, U2](multiplier: Double) extends QtyConversion[U1, U2] {
    override def convert(q1: Quantity[U1])(implicit conv: QtyConversion[U1, U2], d2: Describe[U2]): Quantity[U2] =
      Quantity[U2](q1.value * multiplier)
  }

  object QtyConversion extends LengthConversionInstances with MassConversionInstances {
    def apply[U1, U2](multiplier: Double) = new QtyConversionImpl[U1, U2](multiplier)
  }

  trait LengthConversionInstances {
    implicit val c1 = QtyConversion[KM, KM](1.0)
    implicit val c2 = QtyConversion[MI, MI](1.0)
    implicit val c3 = QtyConversion[MI, KM](1.60934) // 1 mi = 1.60934 km
    implicit val c4 = QtyConversion[KM, MI](1 / 1.60934) // 1 km = 0.62137 mi
  }
  trait MassConversionInstances {
    implicit val c5 = QtyConversion[KG, KG](1.0)
    implicit val c6 = QtyConversion[LB, LB](1.0)
    implicit val c7 = QtyConversion[KG, LB](2.2)
    implicit val c8 = QtyConversion[LB, KG](1 / 2.2)
  }

  trait DoubleLiftSyntax { _: DescribeInstances =>
    implicit class LengthLiftSyntax(value: Double) {
      def km: Quantity[KM] = Quantity[KM](value)
      def mi: Quantity[MI] = Quantity[MI](value)
    }
    implicit class MassLiftSyntax(value: Double) {
      def kg: Quantity[KG] = Quantity[KG](value)
      def lb: Quantity[LB] = Quantity[LB](value)
    }
  }
  object DoubleLiftSyntax extends DoubleLiftSyntax with DescribeInstances

  trait QuantitySyntax {
    implicit class QtyOperations1[U1](a: Quantity[U1]) {
      def to[U2](implicit ev: QtyConversion[U1, U2], d2: Describe[U2]): Quantity[U2] = ev.convert(a)
      def as[U2](implicit ev: QtyConversion[U1, U2], d2: Describe[U2]): Double = to[U2].value
    }

    implicit class QtyOperations2[A: Describe](a: Quantity[A]) {
      def +[B](b: Quantity[B])(implicit conv: QtyConversion[B, A]): Quantity[A] = Quantity[A](a.value + b.to[A].value)
      def -[B](b: Quantity[B])(implicit conv: QtyConversion[B, A]): Quantity[A] = Quantity[A](a.value - b.to[A].value)
      def *(n: Double): Quantity[A] = Quantity[A](a.value * n)
      def /(n: Double): Quantity[A] = Quantity[A](a.value / n)
    }
  }
  object QuantitySyntax extends QuantitySyntax

  object Syntax extends DoubleLiftSyntax with DescribeInstances with QuantitySyntax
}

class PhantomTypeParameterSpec extends AnyFunSpec with Matchers with Checkers {

  // syntax
  import PhantomTypeParameter.DoubleLiftSyntax._
  import PhantomTypeParameter.QuantitySyntax._
  // types
  import PhantomTypeParameter._

  describe("experiments") {
    val q1: Quantity[KM] = 1.km // 1.0 km
    val q2: Quantity[MI] = q1.to[MI] // 0.62137 mi
    val q3: Quantity[KM] = 1.km + 2.km // 3 km
    val q4: Quantity[MI] = 2.mi + 3.mi // 5 mi
    val q5: Quantity[KM] = 1.km + 1.mi // 1.0 + 1.6... = 2.60934 km
    val q6: Quantity[MI] = 1.mi + 1.km // 1.0 + 0.6... = 1.62137 mi
    val d1: Double = q1.as[KM] // 1.0
    val d2: Double = q1.as[MI] // 0.62137

    println(q1)
    println(q2)
    println(q3)
    println(q4)
    println(q5)
    println(q6)
    println(d1)
    println(d2)

    println(1.kg + 1.lb) // 1.4545454545454546 kg
    println(1.lb + 1.kg) // 3.2 lb
    println(1.kg / 2 + 1.lb * 2.2) // 1.5 kg
  }

}
