package conversion

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import scala.util.chaining.scalaUtilChainingOps

object Conversion {

  final case class Fact(from: String, factor: BigDecimal, to: String)
  final case class Query(value: BigDecimal, from: String, to: String)

  class UnitConverter(facts: List[Fact]) {

    private val graph: Map[String, List[(String, BigDecimal)]] = {
      val edges =
        facts
          .flatMap { case Fact(from, factor, to) =>
            List(
              from -> (to   -> factor),
              to   -> (from -> (BigDecimal(1) / factor))
            )
          }

      edges
        .groupMap { case (from, _) => from } { case (_, to) => to }
        .withDefaultValue(Nil)
    }

    def convert(q: Query): Option[BigDecimal] =
      if (q.from == q.to) Some(q.value)
      else findFactor(q.from, q.to).map(x => q.value * x)

    private def findFactor(from: String, to: String): Option[BigDecimal] = {

      @annotation.tailrec
      def go(queue: List[(String, BigDecimal)], visited: Set[String]): Option[BigDecimal] =
        queue match {
          case Nil                       => None
          case (current, factor) :: rest =>
            val nextNodes =
              graph(current)
                .filter { case (next, _) => !visited.contains(next) }
                .map { case (next, edgeFactor) => next -> (factor * edgeFactor) }

            nextNodes.find { case (next, _) => next == to } match {
              case Some((_, resultFactor)) => Some(resultFactor)
              case None                    => go(rest ++ nextNodes, visited ++ nextNodes.map(_._1))
            }
        }

      go(List(from -> BigDecimal(1)), Set(from))
    }
  }

}

class ConversionSpec extends AnyFunSuite with Matchers {

  import Conversion._

  def format(value: BigDecimal) =
    value.setScale(3, BigDecimal.RoundingMode.HALF_UP)

  val facts = List(
    Fact("m", BigDecimal("3.28"), "ft"),
    Fact("ft", BigDecimal("12"), "in"),
    Fact("hr", BigDecimal("60"), "min"),
    Fact("min", BigDecimal("60"), "sec")
  )

  val converter = new UnitConverter(facts)

  test("1") {

    List(
      Query(BigDecimal("2"), "m", "in")      -> Some(78.720),
      Query(BigDecimal("13"), "in", "m")     -> Some(0.330),
      Query(BigDecimal("13"), "in", "hr")    -> None,
      Query(BigDecimal("2"), "hr", "sec")    -> Some(7200.000),
      Query(BigDecimal("120"), "sec", "min") -> Some(2.000),
    )
      .foreach { case (q, exp) =>
        val r = converter.convert(q).map(format)
        r shouldBe exp
      }
  }

}
