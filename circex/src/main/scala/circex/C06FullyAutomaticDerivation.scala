package circex

import io.circe.syntax._
import io.circe.generic.auto._

object C06FullyAutomaticDerivation extends App {

  case class Details(userId: Int)

  val details = Details(33)
  println(details.asJson)

}
