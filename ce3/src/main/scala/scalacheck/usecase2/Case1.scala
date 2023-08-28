package scalacheck.usecase2

import org.scalacheck.Gen

/**
  * https://cucumbersome.net
  */
object Case1 extends App {
  
  val evens: Gen[Int] = Gen.posNum[Int].map(_ * 2)
  val odds: Gen[Int] = evens.map(_ + 1)

  val leftOrRightF5050: Gen[Either[String, Int]] =
    Gen.posNum[Int].flatMap { _ % 2 match {
      case 0 => Gen.posNum[Int].map(Right[String, Int])
      case 1 => Gen.alphaLowerStr.map(Left[String, Int])
    }}

  val leftOrRight4060: Gen[Either[String, Int]] = Gen.frequency(
    4 -> Gen.posNum[Int].map(Right[String, Int]),
    6 -> Gen.alphaLowerStr.map(Left[String, Int])
  )

  val someOfInt:       Gen[Option[Int]] = Gen.some(Gen.posNum[Int])
  val optionalInteger: Gen[Option[Int]] = Gen.option(Gen.posNum[Int])

  val oneOfRanges: Gen[Int] = Gen.oneOf(
    Gen.chooseNum(10, 20),
    Gen.chooseNum(40, 50)
  )

}
