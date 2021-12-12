package scalacheck

import java.util.UUID

import org.scalacheck.Gen

object Guide2Gen extends App {

  /** generators know, HOW generate the values */
  val gInt: Gen[Int] = Gen.choose(-100, 100)
  val gPosInt: Gen[Int] = Gen.posNum[Int]
  val gNegInt: Gen[Int] = Gen.negNum[Int]
  val gLong: Gen[Long] = Gen.choose(0L, 1000L)
  val gPosLng: Gen[Long] = Gen.posNum[Long]
  val gBigDec: Gen[BigDecimal] = Gen.choose(0, 10).map(BigDecimal(_))

  val gC1: Gen[Char] = Gen.alphaChar // alphaUpperChar, alphaLowerChar ...

  val gS1: Gen[String] = Gen.alphaStr // alphaLowerStr, alphaUpperStr ...
  val gS4: Gen[String] = Gen.alphaNumStr
  val gString: Gen[String] = Gen.choose(0, 10).map(_.toString)
  val s1: Gen[String] = Gen.oneOf("Jim", "Jeremy", "Jacky")
  val s2: Gen[String] = Gen.oneOf(Seq("Marta", "Kerry", "Jacky"))

  val gList: Gen[List[Int]] = Gen.choose(1, 5).map(n => List(n, n * 10, n * 100))
  val gList1: Gen[List[Int]] = Gen.listOf(Gen.choose(-100, 100))
  val gList2: Gen[List[Int]] = Gen.listOfN(5, Gen.choose(-100, 100))

  /** because if monad - composition, any logic */
  val genTuple: Gen[(Long, String, UUID)] = for {
    a <- Gen.choose[Long](-100, 100)
    b <- Gen.alphaStr
    c <- Gen.uuid
  } yield (a, b, c)

  /** can write my own construction to create generator */
  val genMy: Gen[String] = for {
    a <- Gen.choose(0, 100)
    b = a * 2
    c = b.toString
  } yield c

  /** own type - Operation */
  object Operation extends Enumeration {
    type Operation = Value
    val ADD, SUB, MUL, DIV = Value
  }

  /** own type - Calculator */
  case class Calculator(a: Int, b: BigDecimal) {
    import Operation.Operation

    def calc(op: Operation): String = {
      val r = op match {
        case Operation.ADD => a + b
        case Operation.SUB => a - b
        case Operation.MUL => a * b
        case Operation.DIV => a / b
        case _             => ???
      }
      s"$a + $b = $r"
    }
  }

  /** own, frequency based generator Operation.MUL - twice as often
    */
  val genOp: Gen[Operation.Value] = Gen.frequency(
    5 -> Operation.ADD,
    1 -> Operation.SUB,
    2 -> Operation.MUL,
    4 -> Operation.DIV,
  )

  /** own generator of calculators */
  val genCalc: Gen[Calculator] = for {
    a <- gInt
    b <- gBigDec
    c = Calculator(a, b)
  } yield c

  /** Optional because, there is no guarantee that all gens will provide a value */
  val s: Option[Calculator] = genCalc.sample

}
