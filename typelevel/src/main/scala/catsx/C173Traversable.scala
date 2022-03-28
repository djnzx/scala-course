package catsx

import cats._
import cats.effect._
import cats.implicits._

object C173Traversable extends App {

  val hosts = List(
    "alpha.ibm.com",
    "beta.ibm.com",
    "lambda.ibm.com",
  )

  val r: (List[Int], List[Int]) = (1 to 10)
    .toList
    .partitionMap(x => Either.cond(x % 2 == 0, x, x))

  pprint.pprintln(r)

}
