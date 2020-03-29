package cats

import cats.data.Reader

object C114Reader extends App {

  case class Cat(name: String, favoriteFood: String)
  val catName: Reader[Cat, String] = Reader(cat => cat.name)
  val greetKitty: Reader[Cat, String] = catName.map(name => s"Hello ${name}")
  val feedKitty: Reader[Cat, String] = Reader(cat => s"Have a nice bowl of ${cat.favoriteFood}")

  val cat = Cat("Barcelona", "Royal")

  val greetAndFeed: Reader[Cat, String] =
    for {
      greet <- greetKitty
      feed <- feedKitty
    } yield s"$greet. $feed."

  val r: Id[String] = greetAndFeed.run(cat)
  println(r)

}
