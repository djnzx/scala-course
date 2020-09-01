package pfps

import cats.implicits._

object A001 {
  case class User(name: Username, email: Email, id: Long = 0)
  case class Username private (value: String) extends AnyVal
  case class Email private (value: String) extends AnyVal

  def mkUsername(value: String): Option[Username] =
    if (value.nonEmpty) Username(value).some else none[Username]

  def mkEmail(value: String): Option[Email] =
    if (value.contains("@")) Email(value).some else none[Email]

  def lookup[F[_]](username: Username, email: Email): F[Option[User]] = ???
  def lookup2(username: Username, email: Email): Option[User] = ???

  val u2: Option[User] = for {
    n <- mkUsername("aeinstein")
    e <- mkEmail("aeinstein@research.com")
  } yield User(n, e)


  val u3: Option[Option[User]] = (
    mkUsername("aeinstein"),
    mkEmail("aeinstein@research.com")
    ).mapN {
    case (username, email) => lookup2(username, email)
  }



}
