package catsx.c114reader

import cats.Id
import cats.data.Reader

/**
  * reader is just a wrapper of f: A => B
  */
object Reader1Intro extends App {

  case class Cat(name: String, favoriteFood: String)

  /** definition */
  val catName: Reader[Cat, String] = Reader(cat => cat.name)
  val greet: Reader[Cat, String] = catName.map(name => s"Hello $name")
  val feed: Reader[Cat, String] = Reader(cat => s"Have a nice bowl of ${cat.favoriteFood}")

  val cat = Cat("Barcelona", "Royal")

  /** flatMap composition:
    * input parameter being fed to all readers
    */
  val greetAndFeed: Reader[Cat, (String, String)] =
    for {
      msg1 <- greet
      msg2  <- feed
    } yield (msg1, msg2)

  /** run the composition */
  val r1: Id[(String, String)] = greetAndFeed.run(cat)
  val r2:    (String, String)  = greetAndFeed.apply(cat)
  val r3:    (String, String)  = greetAndFeed(cat)

}
