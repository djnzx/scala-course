package scalacheck

import org.scalacheck.{Gen, Prop, Properties}
import scalacheck.Idea.Operation.Operation

object Idea extends App {
  object Operation extends Enumeration {
    type Operation = Value
    val ADD, SUB, MUL, DIV = Value
  }
  
  case class Calculator(a: Int, b: BigDecimal) {
    def calc(op: Operation): String = {
      val r = op match {
        case Operation.ADD => a + b 
        case Operation.SUB => a - b
        case Operation.MUL => a * b
        case Operation.DIV => a / b
      }
      s"$a + $b = $r"
    }
  }
  
  /** generators know, HOW generate the values */
  val gInt:    Gen[Int]       = Gen.choose(-100, 100)
  val gLong:   Gen[Long]      = Gen.choose(0L, 1000L)
  val gString: Gen[String]    = Gen.choose(0, 10).map(_.toString)
  val gBigDec: Gen[BigDecimal]= Gen.choose(0, 10).map(BigDecimal(_))
  val gList:   Gen[List[Int]] = Gen.choose(1,5).map(n => List(n, n*10, n*100))
  val gCh:     Gen[String]    = Gen.alphaLowerStr
  val gList1:  Gen[List[Int]] = Gen.listOf(Gen.choose(-100, 100))
  val gList2:  Gen[List[Int]] = Gen.listOfN(5, Gen.choose(-100, 100))
  /**  can write my own construction to create generator */
  val gMy: Gen[String] = for {
    a <- Gen.choose(0, 100)
    b = a * 2
    c = b.toString
  } yield c
  
  /**
    * Operation.MUL - twice as often
    */
  val gOp: Gen[Operation.Value] = Gen.frequency(
    5 -> Operation.ADD,
    1 -> Operation.SUB,
    2 -> Operation.MUL,
    4 -> Operation.DIV,
  )
  
  val gCalc: Gen[Calculator] = for {
    a <- gInt
    b <- gBigDec
    c = Calculator(a, b)
  } yield c
  
  
  
  val s: Option[Calculator] = gCalc.sample
  pprint.log(s)
}

object AddProperty extends Properties("calc.add.prop") {
  property("add") = Prop.forAll { (a: Int, b: Int) =>
    a + b == b + a
  }
}

/**
  * inputs 
  * - are random 
  * - run in parallel
  */
object AddAssocProperty extends Properties("calc.add.assoc.prop") {
  property("add") = Prop.forAll { (a: Int, b: Int, c: Int) =>
    (a + b) + c == a + (b + c)
  }
}

