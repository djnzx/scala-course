package cats101

import cats.data.{NonEmptyList, Validated}
import cats.implicits._

object C152Validated0 extends App {

  type L = NonEmptyList[String]

  val ok1: Validated[L, Int] = 10.valid
  val ok2: Validated[L, String] = "Jim".valid
  val e1 = Validated.invalid(NonEmptyList.of("wrong 1"))
  val e2 = Validated.invalid(NonEmptyList.of("wrong 2"))

  val combinedErr1 = (ok1, ok2, e1).tupled
  val combinedErr2 = (ok1, ok2, e1, e2).tupled
  val combinedOk = (ok1, ok2).tupled

  pprint.pprintln(combinedOk)
  pprint.pprintln(combinedErr1)
  pprint.pprintln(combinedErr2)

}
