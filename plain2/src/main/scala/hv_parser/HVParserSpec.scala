package hv_parser

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class HVParserSpec extends AnyFunSpec with Matchers {

  import HVParser._

  object HeadersData {

    /** here is the list of columns in order we expect them to have */
    val interestedIn0: Seq[Value] =
      Seq("A", "B", "C")

    /** represented in set to achieve O(1) contains implementation */
    val interestedIn1: Set[Value] =
      interestedIn0.toSet

    /** represented in map to get orders */
    val interestedIn2: Map[Header, Int] =
      interestedIn0
        .zipWithIndex
        .toMap
  }

  import HeadersData._

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

  it("find indexes of desired columns") {
    findColumns(headers, interestedIn1) shouldEqual dataPositions
  }

  it("get pairs H -> V unsorted (intermediate test step)") {
    pickHv(rawData, dataPositions) shouldEqual data
  }

  it("get pairs H -> V sorted (intermediate test step)") {
    pickHvSorted(rawData, dataPositions, interestedIn2) shouldEqual dataSorted
  }

  it("4") {
    pickVOnlySorted(rawData, dataPositions, interestedIn2) shouldEqual dataOnlySorted
  }

  it("remap columns - final implementation") {
    val mapper = createMapper(headers, interestedIn0)

    mapper(rawData) shouldEqual dataOnlySorted
    mapper("qwertyuiasdfghjk".toVector.map(_.toString)) shouldEqual "tfs".toVector.map(_.toString)
  }

  describe("lazy v2") {
    it("remap columns - final implementation V2") {
      createMapper2(headers, Seq("1", "2")) shouldEqual None
    }

  }
}
