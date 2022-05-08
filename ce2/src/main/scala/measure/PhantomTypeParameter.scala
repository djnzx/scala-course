package measure

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.Checkers
import scala.reflect.ClassTag

object PhantomTypeParameter {

  trait Measure
  trait Length extends Measure
  trait KM extends Length
  trait MI extends Length

  final case class Quantity[U1: ClassTag](value: Double) {
    def to[U2: ClassTag](implicit ev: QtyConversion[U1, U2]): Quantity[U2] = ev.convert(this)
    def as[U2: ClassTag](implicit ev: QtyConversion[U1, U2]): Double = to[U2].value
    override def toString: String = s"$value ${implicitly[ClassTag[U1]].runtimeClass.getSimpleName.toLowerCase}"
  }

  abstract class QtyConversion[U1: ClassTag, U2: ClassTag] private (val multiplier: Double) {
    def convert(a: Quantity[U1])(implicit conv: QtyConversion[U1, U2]): Quantity[U2] = Quantity[U2](a.value * conv.multiplier)
  }
  object QtyConversion {
    implicit val c1 = new QtyConversion[KM, KM](1.0) {}
    implicit val c2 = new QtyConversion[MI, MI](1.0) {}
    implicit val c3 = new QtyConversion[KM, MI](0.62137) {} // 1 km = 0.62137 mi
    implicit val c4 = new QtyConversion[MI, KM](1.60934) {} // 1 mi = 1.60934 km
  }

  implicit class QtySyntax(value: Double) {
    def km: Quantity[KM] = Quantity[KM](value)
    def mi: Quantity[MI] = Quantity[MI](value)
  }
  implicit class QtyOperations[A: ClassTag](a: Quantity[A]) {
    def +[B: ClassTag](b: Quantity[B])(implicit conv: QtyConversion[B, A]): Quantity[A] = Quantity[A](a.value + b.to[A].value)
  }
}

class PhantomTypeParameterSpec extends AnyFunSpec with Matchers with Checkers {

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
  }

}
