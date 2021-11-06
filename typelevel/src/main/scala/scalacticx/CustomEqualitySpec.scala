package scalacticx

import org.scalactic.Equality
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class CustomEqualitySpec extends AnyFunSpec with Matchers {

  /** actually it can be done for any type: Car, Student, etc */
  def precision(d: Double): Equality[Double] = new Equality[Double] {
    override def areEqual(x: Double, y: Any): Boolean = y match {
      case y: Double => math.abs(x - y) <= d
      case _ => false
    }
  }

  it("precision 0.01") {
    implicit val p = precision(0.01)
    1.005 === 1.006 shouldEqual true
    1 === 1.02 shouldEqual false
  }

  it("precision 0.001") {
    implicit val p = precision(0.001)
    1.001 === 1.0015 shouldEqual true
    1 === 1.002 shouldEqual false
  }

}
