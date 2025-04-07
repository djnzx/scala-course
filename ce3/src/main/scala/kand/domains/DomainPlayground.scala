package kand.domains

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

object DomainPlayground {

  sealed trait Domain
  case class Point(value: Double)       extends Domain
  case class Points(values: Seq[Point]) extends Domain

  case class Range(min: Double, max: Double) extends Domain
  case class Ranges(values: Seq[Range])      extends Domain

  case class Domains(values: Seq[Domain]) extends Domain

  def intersect(r1: Range, r2: Range): Option[Domain] = {

    if (r1.max < r2.min) return None
    if (r2.max < r1.min) return None

    if (r1.max == r2.min) return Some(Point(r1.max))
    if (r2.max == r1.min) return Some(Point(r2.max))

    val l = r1.min max r2.min
    val r = r1.max min r2.max

    Some(Range(l, r))
  }

  def intersect(rs1: Ranges, rs2: Ranges): Option[Domains] = {
    // naive impl
    val cross = rs1.values
      .flatMap { r1 =>
        rs2.values.flatMap { r2 =>
          intersect(r1, r2)
        }
      }
    // sort? set? union, duplicates?
    if (cross.isEmpty) return None

    Some(Domains(cross))
  }

}

class DomainPlayground extends AnyFunSuite with Matchers {
  import DomainPlayground._

  test("no cross") {
    val r1 = Range(1, 5)
    val r2 = Range(10, 15)
    intersect(r1, r2) shouldBe None
    intersect(r2, r1) shouldBe None
  }

  test("cross produces single point") {
    val r1 = Range(1, 5)
    val r2 = Range(5, 10)
    intersect(r1, r2) shouldBe Some(Point(5))
    intersect(r2, r1) shouldBe Some(Point(5))
  }

  test("general cases") {
    val r1 = Range(2, 7)
    val r2 = Range(5, 9)
    intersect(r1, r2) shouldBe Some(Range(5, 7))
    intersect(r2, r1) shouldBe Some(Range(5, 7))
  }

  test("intersect ranges 1") {
    val d1 = Ranges(Seq(Range(0, Double.MaxValue)))
    val d2 = Ranges(Seq(
      Range(1,2),
      Range(3,4)
    ))

    val d3 = intersect(d1, d2)
    pprint.log(d3)
  }

  test("intersect ranges 2") {
    val d1 = Ranges(Seq(
      Range(1,4),
      Range(6,10),
    ))
    val d2 = Ranges(Seq(
      Range(2,5),
      Range(7,11)
    ))

    val d3 = intersect(d1, d2)
    pprint.log(d3)
  }

}
