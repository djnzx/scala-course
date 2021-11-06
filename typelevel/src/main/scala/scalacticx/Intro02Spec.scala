package scalacticx

import org.scalactic._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class Intro02Spec extends AnyFunSpec with Matchers {

  it("typed compare") {
    import TypeCheckedTripleEquals._

    1 should not equal Some(1)
  }

  it("TraversableEqualityConstraints") {
    import TraversableEqualityConstraints._
    List(1, 2, 3) shouldEqual Vector(1, 2, 3)
  }

}
