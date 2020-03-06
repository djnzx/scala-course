package scalacheck

import org.scalacheck.Gen._
import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Gen, Properties}

object Check extends App {

  def getRandomIntFrom1To100: Int = scala.util.Random.nextInt(100) + 1
  def increaseRandomly(i: Int) = {
    val randomNum = getRandomIntFrom1To100
//    i + randomNum
        i + randomNum.toLong
  }

}

object CheckSpec extends Properties("IncreaseRandomlySpec") {

  // “What are some general properties or observations I can make about how increaseRandomly works?”
  // Regardless of how many times it's called, the result of increaseRandomly should always be greater than the number given to it as an input parameter
  // ScalaCheck refers to this general observation as a “property” of the function.
  property("increaseRandomly") = forAll { input: Int =>
    val result = Check.increaseRandomly(input)
    result > input
  }

  val chars_az: Gen[Char] = choose('a', 'z')
  val littleInts: Gen[Int] = choose(0, 99)
  val intsGreaterThan1: Gen[Int] = choose(2, 10000)  //2147483647
  val nonZeroOneInts: Gen[Int] = Arbitrary.arbitrary[Int] suchThat { i => i != 0 && i != 1 }


}
