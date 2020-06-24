package fp_red.red10

import fp_red.c_answers.c06state.{RNG, State}
import fp_red.red08.Prop.Passed
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class MonoidSpec extends AnyFunSpec with Matchers {

  import fp_red.red08.Gen
  import fp_red.red08.Prop
  import Monoid._
  
  describe("Monoids tests") {
    it("intAddition:") {
      
      /**
        * so, gen - actually is a function which takes a random generator RNG
        * and generates a value in the range [1, 10)
        * 
        * gen.sample: State[RNG, Int]
        * gen.sample: RNG => (Int, RNG)
        */
      val gen: Gen[Int] = Gen.choose(1, 10)
      
      /**
        * prop - is a property which should be run further
        */
      val prop: Prop = monoidLaws(intAddition, gen)

      /**
        * run this function
        */
      val res: Prop.Result = prop.run(100, 10, RNG.Simple(1))

      /**
        * actual check
        */
      res shouldBe Passed
    }
  }

}
