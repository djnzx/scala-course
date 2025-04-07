package regularexpressions

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scala.util.matching.Regex

object ExploreRegex {

  val reg: Regex = "\\d{3}".r

}

class ExploreRegexSpec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  import ExploreRegex._

  val s1 = "Received error response from upstream error with status '500'"
  val s2 = "Received error response from upstream error with status '409'"

  test("1") {
    val x: Option[String] = reg.findFirstIn(s1)
    pprint.log(x)
  }
  test("2") {
    val x: Option[String] = reg.findFirstIn(s2)
    pprint.log(x)
  }
  test("3") {

    reg.findFirstIn(s2) match {
      case Some(value) => ???
      case None => ???
    }

  }
}
