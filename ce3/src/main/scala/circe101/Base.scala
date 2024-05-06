package circe101

import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

trait Base extends AnyFunSuite with Matchers with Inside with ScalaCheckPropertyChecks {

}
