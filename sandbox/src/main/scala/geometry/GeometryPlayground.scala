package geometry

import cats.implicits.catsSyntaxTuple3Semigroupal
import common.Base
import org.scalacheck.Gen

class GeometryPlayground extends Base with GeometryFundamentals {

  val intG: Gen[Int] = Gen.choose[Int](0, 9)

  case class Pt(x: Int, y: Int, z: Int)

  /** point generator */
  implicit val pointGen0: Gen[Pt] = (intG, intG, intG).mapN(Pt.apply)

  override implicit val generatorDrivenConfig: PropertyCheckConfiguration =
    PropertyCheckConfiguration(20)

  def distance(pt1: Pt, pt2: Pt): Double =
    distance(pt1.x - pt2.x, pt1.y - pt2.y, pt1.z - pt2.z)

  def next(pt: Pt, distance: Int): Option[Pt] = ???

  test("1") {

    forAll { (pt1: Pt, pt2: Pt) =>
      println(pt1 -> pt2 -> distance(pt1, pt2))
    }

  }

}
