package catsx

import cats.Semigroupal
import cats.instances.future._ // for Semigroupal
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object C149Semi extends App {

  // they started executing simultaneously
  val futurePair: Future[(String, Int)] = Semigroupal[Future].product(Future("Hello"), Future(123))

  val r: (String, Int) = Await.result(futurePair, 1.second)


  import cats.syntax.apply._ // for mapN
  case class Cat(
                  name: String,
                  yearOfBirth: Int,
                  favoriteFoods: List[String]
                )

  val futureCat = (
    Future("Garfield"),
    Future(1978),
    Future(List("Lasagne"))
    ).mapN(Cat.apply)
  val cat: Cat = Await.result(futureCat, 1.second)

  import cats.instances.list._
  val r2: List[(Int, Int)] = Semigroupal[List].product(List(1, 2), List(3, 4))

  import cats.instances.either._
  type ErrorOr[A] = Either[Vector[String], A]
  val r3: ErrorOr[(Nothing, Nothing)] = Semigroupal[ErrorOr].product(
    Left(Vector("Error 1")),
    Left(Vector("Error 2")),
  )

  

}
