package catsx.c114reader

import cats.Monad
import cats.data.Reader
import cats.implicits.catsStdInstancesForList
import pprint.{pprintln => println}

object Reader5ifM extends App {

  def first =
    Reader { x: Int => Monad[List].ifM(List(true, true))(x * 10 :: x * 100 :: Nil, Nil) }

  def second =
    Reader { x: Int => Monad[List].ifM(List(true, true))(x - 1 :: x :: x + 1 :: Nil, Nil) }

  val r = for {
    g <- first(4) // run the Reader with param 4 on each element of List(true, true)
    gg <- second(g)
  } yield gg

  println(r)
}
