package fp_red.red11

import fp_red.red06.{RNG, State}
import fp_red.red08.Prop.Passed
import fp_red.red08.{Gen, Prop, SGen}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class FunctorLawSpec extends AnyFunSpec with Matchers {
  describe("functor laws") {
    it("0 how to use Gen/SGen") {
      /**
        * function is aware of generation ONE Int value from 0 to  100
        */
      val gen1: Gen[Int] = Gen.choose(0, 100)

      /**
        * function is aware of generation LIST of Int values by rule `gen1`
        */
      val genList: SGen[List[Int]] = Gen.listOf(gen1)

      /**
        * by specifying size,
        * we `fold` SGen => Gen[List[Int]
        * function is aware of generation LIST of 20 items
        * from 0 to 100
        */
      val list20: Gen[List[Int]] = genList.g(20)

      /**
        * we run our generator and produce a state
        * which should be run
        */
      val state: State[RNG, List[Int]] = list20.sample

      /**
        * we `run` our state
        */
      val state2: (List[Int], RNG) = state.run(RNG.Simple(1))

      /**
        * we extract the outcome
        */
      val (outcome: List[Int], _) = state2

      /**
        * 20 randoms 0 to 100
        */
      pprint.log(outcome)
    }
    
    it("identity for List") {
      
      val lf: FunctorInstances[List] = FunctorInstances.listFunctor
      val prop: Prop = FunctorLaws.identityLaw(lf)(Gen.listOf(Gen.smallInt))
      val r: Prop.Result = prop.run(100, 10, RNG.Simple(1))
      
      r shouldBe Passed
      
    }
  }
}
