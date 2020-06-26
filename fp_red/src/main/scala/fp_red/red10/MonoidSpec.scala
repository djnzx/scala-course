package fp_red.red10

import fp_red.c_answers.c06state.RNG
import fp_red.red08.Prop.Passed
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class MonoidSpec extends AnyFunSpec with Matchers {

  import Monoid._
  import MonoidLaws._
  import fp_red.red08.{Gen, Prop}
  
  describe("Monoids experiments") {
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
  
  describe("isSorted") {
    it("empty = true") {
      val seq = IndexedSeq()
      isOrdered(seq) shouldBe true
    }
    it("sorted = true:1") {
      val seq = IndexedSeq(4)
      isOrdered(seq) shouldBe true
    }
    it("sorted = true:2") {
      val seq = IndexedSeq(2,3)
      isOrdered(seq) shouldBe true
    }
    it("sorted = true:3") {
      val seq = IndexedSeq(2,3,4)
      isOrdered(seq) shouldBe true
    }
    it("unsorted = false:1") {
      val seq = IndexedSeq(1,2,8,4,5,6)
      isOrdered(seq) shouldBe false
    }
    it("unsorted = false:2") {
      val seq = IndexedSeq(8,1)
      isOrdered(seq) shouldBe false
    }
  }
  
  describe("map merge") {
    it("nested map merge") {
      val monoid: Monoid[Map[String, Map[String, Int]]] = mapMergeMonoid(mapMergeMonoid(intAddition))
      val map1 = Map("o1" -> Map("i1" -> 1, "i2" -> 2))
      val map2 = Map("o1" -> Map("i2" -> 3))

      monoid.op(map1, map2) shouldBe Map(
        "o1" -> Map("i1" -> 1, "i2" -> 5)
      )
    }
  }
  
}
