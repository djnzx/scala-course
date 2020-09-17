package catsx.comonad

import cats._
import cats.data._
//import cats.implicits._
import cats.syntax.comonad._
import cats.instances.list._

object Ex1 {

  val x: Int = NonEmptyList.of(1,2,3).extract


}
