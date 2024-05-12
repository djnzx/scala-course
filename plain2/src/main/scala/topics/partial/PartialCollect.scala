package topics.partial

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import scala.util.matching.Regex

object PartialCollect {

  /** https://regex101.com */
  val pairs: Regex = """(\S+)=(\S+)""".r

  def toPairs(origin: String) =
    origin
      .split("\\s+")
      .collect { case pairs(a, b) => a -> b }

  def toPairs2(origin: String) =
    toPairs(origin)
      .flatMap { case (k, v) => v.toIntOption.map(k -> _) }

}

class PartialCollect extends AnyFunSuite with Matchers {

  import PartialCollect._

  test("nothing matched") {
    val test1 = "a b, c d, e"
    toPairs(test1) shouldBe Seq.empty
  }

  test("pairs string") {
    toPairs("a=1 b=2 c=3x d e f") shouldBe Seq("a" -> "1", "b" -> "2", "c" -> "3x")
  }

  test("pairs int(ignore errors)") {
    toPairs2("a=1 b=2 c=3x d e f") shouldBe Seq("a" -> 1, "b" -> 2)
  }

}
