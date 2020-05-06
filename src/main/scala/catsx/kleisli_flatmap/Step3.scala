package catsx.kleisli_flatmap

import cats.data.Kleisli
import cats.instances.list._

object Step3 extends App {

  val combined2b: Kleisli[List, Int, Int] =
    Kleisli(f1) andThen Kleisli(f2) andThen Kleisli(f3)

  val r = combined2b.run(10)
}
