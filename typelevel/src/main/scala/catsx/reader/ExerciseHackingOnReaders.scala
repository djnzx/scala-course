package catsx.reader

import cats.data.Reader
import cats.implicits.catsSyntaxApplicativeId

object ExerciseHackingOnReaders extends App {

  final case class Db(
    usernames: Map[Int, String],
    passwords: Map[String, String]
  )

  type DbReader[A] = Reader[Db, A]

  def findUsername(userId: Int): DbReader[Option[String]] =
    Reader(_.usernames.get(userId))

  def checkPassword(username: String, password: String): DbReader[Boolean] =
    Reader(_.passwords.get(username).contains(password))

  def checkLogin(userId: Int, password: String): DbReader[Boolean] =
    for {
      oUser <- findUsername(userId)
      fact <- oUser match {
        case None => Reader[Any, Boolean](_ => false)
        case Some(user) => checkPassword(user, password)
      }
    } yield fact

  def checkLogin2(userId: Int, password: String): DbReader[Boolean] =
    for {
      oUser <- findUsername(userId)
      fact <- oUser.map(user => checkPassword(user, password)).getOrElse(false.pure[DbReader])
    } yield fact

}
