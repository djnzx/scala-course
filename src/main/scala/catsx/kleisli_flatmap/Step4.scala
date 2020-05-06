package catsx.kleisli_flatmap

import cats.data.Kleisli
import cats.instances.list._

object Step4 extends App {

  val combined2c: Kleisli[List, Int, Int] =
    Kleisli { x: Int => List(x + 1, x - 1) } andThen
      Kleisli { x: Int => List(x, -x) } andThen
      Kleisli { x: Int => List(x * 2, x / 2) }

  val r = combined2c.run(10)
}
