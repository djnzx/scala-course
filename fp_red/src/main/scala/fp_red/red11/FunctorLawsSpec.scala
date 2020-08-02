package fp_red.red11

import fp_red.red06.{RNG, State}
import fp_red.red08.Prop.Passed
import fp_red.red08.{Gen, Prop, SGen}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class FunctorLawsSpec extends AnyFunSpec with Matchers {
  describe("functor laws") {
    it("0 how to use Gen/SGen") {
      /**
        * function is aware of generation ONE Int value from 0 to 100
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
    
    describe("list") {
      val lf: Functor[List] = Functor.listFunctor
      val dataset: SGen[List[Int]] = Gen.listOf(Gen.smallInt)
      val f = (n: Int) => n + 5
      val g = (n: Int) => n * 2
      
      it("identity") {
        FunctorLaws.identityLaw(lf)(dataset)
          .run(100, 10, RNG.Simple(1)) shouldBe Passed
      }
      it("left assoc") {
        FunctorLaws.leftAssocLaw(lf)(f)(dataset)
          .run(100, 10, RNG.Simple(1)) shouldBe Passed
      }
      it("right assoc") {
        FunctorLaws.rightAssocLaw(lf)(f)(dataset)
          .run(100, 10, RNG.Simple(1)) shouldBe Passed
      }
      it("composition") {
        FunctorLaws.compositionLaw(lf)(f, g)(dataset)
          .run(100, 10, RNG.Simple(1)) shouldBe Passed
      }
      
    }
    
    it("identity for Optional") {
      val of: Functor[Option] = Functor.optFunctor
      val opt: Gen[Option[Int]] = Gen.opt(Gen.choose(-10, 10))
      val prop: Prop = FunctorLaws.identityLaw(of)(opt)
      val r: Prop.Result = prop.run(100, 10, RNG.Simple(1))
      r shouldBe Passed
    }
  }
}
