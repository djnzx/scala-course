package guillaumebogard

import cats.implicits._

object ApplicativeApp1 extends App {

  // normal function
  val f: (Int, Char)  => Double = (i, c) => (i + c).toDouble
  // curried one
  val fc: Int => Char => Double = i => c => f(i, c)

  val int:  Option[Int]  = Some(5)
  val char: Option[Char] = Some('a')

  // partially applied
  val partial: Option[Char => Double] = int.map { fc }
  // fully applied
  val fully: Option[Double] = partial.ap(char)

  // or with flatMap:
  val fully2: Option[Double] = for {
    i <- int
    c <- char
  } yield f(i, c)

}
