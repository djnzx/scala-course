package catsx.c002eq

//import cats.Eq
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class C027Eq extends AnyFunSpec with Matchers {

  def precision(delta: Double): cats.Eq[Double] = new cats.Eq[Double] {
    override def eqv(x: Double, y: Double): Boolean = math.abs(x - y) <= delta
  }

  it("1") {
    implicit val p = precision(0.01)

    1.0 === 1.01 shouldEqual true

  }

}
