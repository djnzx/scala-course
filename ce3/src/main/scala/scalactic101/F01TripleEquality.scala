package scalactic101

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class F01TripleEquality extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks{

  test("1") {
    import org.scalactic.TripleEquals._

    /** will not compile ! */
    //if (1 === "2") ???
  }

}
