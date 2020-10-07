package tools.scalacticx

import org.scalactic.Explicitly.after
import org.scalactic.TypeCheckedTripleEquals._
import org.scalactic.{Normalization, NormalizingEquivalence, _}
import org.scalatest.Assertion
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

object Guide3App extends App {
  
  import Guide3._

  /**
    * standard ===
    * obviously, false
    */
  val r1: Boolean = RES === EXP
  println(s"$RES === $EXP: $r1")

  /** 
    * but we can define the rule 
    * how to truncate the result (left part):
    * we can write our custom logic inside
    */ 
  val floorDouble: Normalization[Double] = new Normalization[Double] {
    def normalized(d: Double) = d.floor
  }
  
  /** 
    * to use it with === syntax, 
    * we need to define the way
    * how to normalize for further comparison
    */
  val myDoubleEqu: NormalizingEquivalence[Double] = after being floorDouble

  /**
    * we can use it
    * with === syntax, by providing custom equivalence rule
    * ...true
    */
  val r2: Boolean = (RES === EXP) (myDoubleEqu)
  println(s"($RES === $EXP) (myDoubleEqu): $r2")

  /**
    * or use in tests:
    * ...Succeeded
    */
  val r3: Assertion = (2.1 should === (2.0)) (myDoubleEqu)
  println(s"($RES should === ($EXP)) (myDoubleEqu): $r3")

  // TODO: some issues with usage in tests
}
