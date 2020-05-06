package catsx.kleisli_flatmap

import cats.data.Kleisli
import cats.instances.list._

object Step2 extends App {

  // we can lift to Kleilsi
  val step1:  Kleisli[List, Int, Int] = Kleisli(f1)
  val step2:  Kleisli[List, Int, Int] = Kleisli(f2)
  val step2s: Kleisli[List, Int, String] = Kleisli(f2a)
  val step3:  Kleisli[List, Int, Int] = Kleisli(f3)

  // having Kleisli we can write:
  val combined2a: Kleisli[List, Int, Int] =
    step1 andThen step2 andThen step3

  // run them in following way
  val r = combined2a.run(10)
}
