package cats101.c114reader

import cats.data.Reader
import cats.implicits.catsSyntaxApplicativeId

object Reader4ExerciseHackingOnReaders extends App {

  final case class Db(
    usernames: Map[Int, String],
    passwords: Map[String, String]
  )

  type DbReader[A] = Reader[Db, A]

  def findUsername(userId: Int): DbReader[Option[String]] =
    Reader(db => db.usernames.get(userId))

  def checkPassword(username: String, password: String): DbReader[Boolean] =
    Reader(db => db.passwords.get(username).contains(password))

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

  val users = Map(1 -> "dade", 2 -> "kate", 3 -> "margo")
  val passwords = Map("dade" -> "zerocool", "kate" -> "acidburn", "margo" -> "secret")

  val db = Db(users, passwords)

  val r1 = checkLogin(1, "zerocool").run(db)
  val r2 = checkLogin(2, "zerocool").run(db)

  assert(r1 == true)
  assert(r2 == false)
}
