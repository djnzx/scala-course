package hv_parser

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class HVParserSpec extends AnyFunSpec with Matchers {

  import HVParser._

  val headers = List("1", "Q", "W", "E", "A", "S", "D", "Z", "X", "C", "V", "B")
  val rawData = Vector("1", "q", "w", "e", "a", "s", "d", "z", "x", "c", "v", "b")
  val dataPositions = Set(
    "A" -> 4,
    "B" -> 11,
    "C" -> 9,
  )
  val data = Set(
    "C" -> "c",
    "A" -> "a",
    "B" -> "b",
  )
  val dataSorted = Vector(
    "A" -> "a",
    "B" -> "b",
    "C" -> "c",
  )
  val dataOnlySorted = Vector("a", "b", "c")

  it("1") {
    find(headers) shouldEqual dataPositions
  }

  it("2") {
    pick(rawData, dataPositions) shouldEqual data
  }

  it("3") {
    pickSorted(rawData, dataPositions) shouldEqual dataSorted
  }

  it("4") {
    pickSortedData(rawData, dataPositions) shouldEqual dataOnlySorted
  }

}
