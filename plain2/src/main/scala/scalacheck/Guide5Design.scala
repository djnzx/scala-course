package scalacheck

import org.scalacheck.Gen.{chooseNum, oneOf}
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Prop.{AnyOperators, forAll, propBoolean}

object Guide5Design {

  case class Dice(x: Int)
  case class Hand(p1: Dice, p2: Dice, p3: Dice, p4: Dice, p5: Dice)
  
  val gAll: List[Dice] = List(1,2,3,4,5,6).map(Dice)
  val gDice: Gen[Dice] = Gen.choose(1, 6).map(Dice)
  val gHand: Gen[Hand] = for {
    a <- gDice
    b <- gDice
    c <- gDice
    d <- gDice
    e <- gDice
  } yield Hand(a, b, c, d, e)
  val gYahtzee: Gen[Hand] = oneOf(gAll).map(x => Hand(x, x, x, x, x))
  val getThreeOfAKind: Gen[Hand] = for {
    d1 <- oneOf(gAll)
    d2 <- oneOf(gAll diff List(d1))
    d3 <- oneOf(gAll diff List(d1, d2))
  } yield Hand(d1, d1, d1, d2, d3)
  
  val orderedGens: List[Gen[Hand]] = List(
    gYahtzee,
//    gStraight,
//    gFullHouse,
//    gFourOfAKind,
    getThreeOfAKind
  )
  implicit val arbHand: Arbitrary[Hand] = Arbitrary(gHand)
  implicit val arbDice: Arbitrary[Dice] = Arbitrary(gDice)
  
  /** our logic to test */
  def winner(h1: Hand, h2: Hand): Hand = ???

  forAll { (y: Dice, fhA: Dice, fhB: Dice) => 
//    fhA != fhB ==>
      
    val yahtzee: Hand = Hand(y, y, y, y, y)
    val fullHouse: Hand = Hand(fhA, fhA, fhA, fhB, fhB)
    
    (winner(yahtzee, fullHouse) ?= yahtzee) &&
    (winner(fullHouse, yahtzee) ?= yahtzee)
  }
  
  forAll(chooseNum[Int](1, orderedGens.length - 1)) { idx => 
    val (winGens, loseGens): (List[Gen[Hand]], List[Gen[Hand]]) = orderedGens.splitAt(idx)
    
    
    
    forAll(oneOf(winGens), oneOf(loseGens)) { (wh, lh) =>
      (winner(wh.sample.getOrElse(???), lh.sample.getOrElse(???)) ?= wh.sample.getOrElse(???)) &&
        (winner(lh.sample.getOrElse(???), wh.sample.getOrElse(???)) ?= wh.sample.getOrElse(???))
    }
  }

}
