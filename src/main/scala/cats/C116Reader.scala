package cats

import cats.data.Reader
import cats.syntax.applicative._

object C116Reader extends App {
  case class Db(usernames: Map[Int, String], passwords: Map[String, String])

  type DbReader[A] = Reader[Db, A]

  def findUsername(userId: Int): DbReader[Option[String]] =
    Reader { db: Db => db.usernames.get(userId) }

  def checkPassword(username: String, password: String): DbReader[Boolean] =
    Reader { db: Db => db.passwords.get(username)
      .contains(password)
//      .exists(p => p == password)
//      .map(p => p == password).getOrElse(false)
    }

  def checkLogin(userId: Int, password: String): DbReader[Boolean] = for {
    name <- findUsername(userId)
    ok   <- name.map { name => checkPassword(name, password) }.getOrElse(false.pure[DbReader])
  } yield ok

  val users = Map( 1 -> "dade", 2 -> "kate", 3 -> "margo")
  val passwords = Map( "dade" -> "zerocool", "kate" -> "acidburn", "margo" -> "secret")

  val db = Db(users, passwords)
  val vr1 = checkLogin(1, "zerocool").run(db)
  val vr2 = checkLogin(2, "zerocool").run(db)
  println(vr1) // true
  println(vr2) // false

}
