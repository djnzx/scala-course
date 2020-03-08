package cats.kleisli_flatmap

import cats.data.Kleisli
import cats.instances.list._
import cats.implicits._

object Step5 extends App {

//  val combined2c: Kleisli[List, Int, Int] =
//    Kleisli { x: Int => List(x + 1, x - 1) } >>>
//      Kleisli { x: Int => List(x, -x) } >>>
//      Kleisli { x: Int => List(x * 2, x / 2) }
//
//  val r = combined2c.run(10)
}
