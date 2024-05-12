package winitzki

import io.chymyst.ch._

import scala.reflect.runtime.universe._

class CurryHoward1 extends Base {

  test("1") {
    def f[X, Y]: X => Y => X = implement

    pprint.log(f.lambdaTerm.prettyPrint)
    pprint.log(reify(f[Int, String]))
  }

  test("2") {
    def f[A, B]: ((((A => B) => A) => A) => B) => B = implement
    // a ⇒ a (b ⇒ b (c ⇒ a (d ⇒ c)))

    pprint.log(f.lambdaTerm.prettyPrint)
    pprint.log(reify(f[Int, String]))
  }

}
