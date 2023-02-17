package vigil_test

trait LineParser {
  def parse: String => Option[(Int, Int)]
}

object LineParser {
  import scala.util.matching.Regex
  private val csvRx: Regex = """^(\d+),(\d+)$""".r
  private val tsvRx: Regex = """^(\d+)\t(\d+)$""".r

  private def mkPair(s1: String, s2: String) = s1.toInt -> s2.toInt
  private type PartialExtractor = PartialFunction[String, (Int, Int)]
  private val csv: PartialExtractor = { case csvRx(k, v) => mkPair(k, v) }
  private val tsv: PartialExtractor = { case tsvRx(k, v) => mkPair(k, v) }

  object CSVLineParser extends LineParser {
    override def parse = csv.lift
  }

  object TSVLineParser extends LineParser {
    override def parse = tsv.lift
  }
}

object LineParserTest extends App {
  import LineParser._
  import pprint._

  val x = "123,456"
  val p1 = CSVLineParser.parse(x)
  pprintln(p1)

  val y = "456\t789"
  val p2 = TSVLineParser.parse(y)
  pprintln(p2)
}
