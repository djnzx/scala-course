package trigonometry

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class Trigonometry extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  import scala.math._

  def round(x: Double, nDigits: Int): Double = {
    val k = pow(10, nDigits)
    (x * k).round.toDouble / k
  }
  def round1(x: Double): Double = round(x, 1)
  def round2(x: Double): Double = round(x, 2)

  test("degree => radians") {
    Seq(
      0,   // 0
      45,  // Pi/4
      90,  // Pi/2
      180, // Pi
      360, // 2*Pi
    )
      .map(_.toRadians)
      .foreach(x => pprint.log(x))
  }

  test("radians => degree") {
    Seq(
      0,      // 0
      Pi / 4, // 45
      Pi / 2, // 90
      Pi,     // 180
      2 * Pi, // 360
    )
      .map(_.toDegrees)
      .foreach(x => pprint.log(x))
  }

  test("sin, cos, tan requires radians") {
    Seq(
      0,
      45, // 1/sqrt(2)
      90,
      180,
      270,
      360,
    )
      .map(x => (x, x.toRadians))
      .map { case x @ (_, r) => x -> (sin(r), cos(r), tan(r)) }
      .foreach(x => pprint.log(x))
  }

  test("asin, acos return radians") {
    Seq(
      1.0, // 0
      0.9, // 25
      0.8, // 37
      0.7, // 45
      0.5, // 60
    )
      .map(x => acos(x))
      .map(_.toDegrees)
      .foreach(x => pprint.log(x))
  }

}
