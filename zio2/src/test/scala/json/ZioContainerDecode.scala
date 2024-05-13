package json

import cats.Id
import cats.implicits.catsSyntaxEitherId
import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import zio.json.DecoderOps

class ZioContainerDecode extends AnyFunSuite with Matchers with Inside {

  test("List[Int]") {
    "[1,2,3,3]".fromJson[List[Int]] shouldBe List(1, 2, 3, 3).asRight
  }

  test("Vector[Int]") {
    "[1,2,3,3]".fromJson[Vector[Int]] shouldBe Vector(1, 2, 3, 3).asRight
  }

  test("Set[Int]") {
    "[1,2,3,3]".fromJson[Set[Int]] shouldBe Set(1, 2, 3).asRight
  }

  test("Id[Int]") {
    "1".fromJson[Id[Int]] shouldBe Id(1).asRight
    "1".fromJson[Id[Int]] shouldBe 1.asRight
  }

}
