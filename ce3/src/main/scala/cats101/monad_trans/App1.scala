package cats101.monad_trans

import cats.Monad
import cats.syntax.applicative._ // for pure
import cats.syntax.flatMap._ // for flatMap

object App1 extends App {

  case class User(name: String)

  def lookupUser(id: Long): Option[User] = ???

  def lookupUserName1(id: Long): Either[Error, Option[String]] = {
    val optu: Option[User] = lookupUser(id)
    val n: Option[String] = optu.map(u => u.name)
    val r: Either[Error, Option[String]] = n match {
      case Some(value) => Right(Some(value))
      case None => Left[Error, Option[String]](new Error)
    }
    r
  }

  def lookupUserName2(id: Long): Either[Error, Option[String]] = {
    val optu: Option[User] = lookupUser(id)
    val opts: Option[String] = for {
      optUser <- optu
      name = optUser.name
    } yield name
    Right(opts)
  }

  def lookupUserName3(id: Long): Either[Error, Option[String]] = {
    Right(
      for {
        optUser <- lookupUser(id)
        name = optUser.name
      } yield name
    )
  }



}
