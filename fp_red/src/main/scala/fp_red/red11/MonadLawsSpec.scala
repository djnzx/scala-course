package fp_red.red11

import fp_red.red06.RNG
import fp_red.red08.{Gen, Prop}
import fp_red.red08.Prop.Passed
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class MonadLawsSpec extends AnyFunSpec with Matchers {
  describe("monad laws") {
    import Monad._
    import MonadLaws._

    /**
      * list of 100 items 0 to 1000 
      */
    val dataset: Gen[List[Int]] =
      Gen.listOfN(100, Gen.choose(0,1000))

    val props: Seq[(String, Prop)] = Vector(
      "left identity"  -> leftIdentityLaw(listMonad)(dataset),
      "right identity" -> rightIdentityLaw(listMonad)(dataset)(x => List(x-1, x+1)),
      "associativity"  -> associativityLaw(listMonad)(dataset)(
        (x: Int) => List(x-1, x+1),
        (x: Int) => List(x.toString)
      )
    )
    
    it("props") {
      props.forall((p: (String, Prop)) =>
        p._2.run(100,10, RNG.Simple(1)) == Passed
      )
    }
    
  }
}
