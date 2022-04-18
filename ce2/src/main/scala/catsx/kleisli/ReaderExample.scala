package catsx.kleisli

import cats.Id
import cats.data.{Kleisli, Reader}
import cats.implicits.toComposeOps
import pprint.{pprintln => println}

/**
  * Reader [      -A, B] = ReaderT[Id, A, B]
  * ReaderT[F[_], -A, B] = Kleisli[F, A, B]
  * Reader [      -A, B] = Kleisli[F, A, B]
  */
object ReaderExample extends App {
  val upper: Reader[String, String] = Reader((text: String) => text.toUpperCase)
  val greet: Reader[String, String] = Reader((name: String) => s"Hello $name")

  val comb1: Kleisli[Id, String, String] = upper.compose(greet)
  val comb2: Kleisli[Id, String, String] = upper.andThen(greet)

  val comb3 = upper >>> greet
  val comb4 = greet >>> upper

  val name = "Alex"
  println(comb3(name))
  println(comb4(name))

}
