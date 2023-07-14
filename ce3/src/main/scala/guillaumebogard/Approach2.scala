package guillaumebogard

import java.util.Date

import cats.Applicative
import cats.data.{EitherT, ReaderT, StateT}
import cats.effect.IO
import cats.instances._
import cats.syntax.flatMap._
import cats.implicits._
import cats.mtl._
import cats.mtl.implicits._

object Approach2 extends App {

  case class User(name: String, isAdmin: Boolean)

  sealed trait AuthenticationError
  case object WrongUserName extends AuthenticationError
  case object WrongPassword extends AuthenticationError
  final case class ExpiredSubscription(expirationDate: Date) extends AuthenticationError
  case object BannedUser extends AuthenticationError

  def findUserByName(username: String): IO[Either[AuthenticationError, User]] = ???
  def checkPassword2(user: User, password: String): IO[Either[AuthenticationError, Unit]] = ???
  def checkSubscription(user: User): IO[Either[AuthenticationError, Unit]] = ???
  def checkUserStatus(user: User): IO[Either[AuthenticationError, Unit]] = ???

  // trash
//  def authenticate0(userName: String, password: String): IO[Either[AuthenticationError, User]] =
//    findUserByName(userName).flatMap({
//      case Right(user) => checkPassword(user, password).flatMap({
//        case Right(_) => checkSubscription(user).flatMap({
//          case Right(_) => checkUserStatus(user).map(_ => Right(user))
//          case Left(err) => IO.pure(Left(err))
//        })
//        case Left(err) => IO.pure(Left(err))
//      })
//      case Left(err) => IO.pure(Left(err))
//    })

  // monad transformers
  val userIO: IO[Either[AuthenticationError, User]] = IO.pure(Left(WrongUserName))
  val myEitherT: EitherT[IO, AuthenticationError, User] = EitherT(userIO)
  val userIoAgain: IO[Either[AuthenticationError, User]] = myEitherT.value

  def authenticate(userName: String, password: String): EitherT[IO, AuthenticationError, User] =
    for {
      user <- EitherT(findUserByName(userName))
      _    <- EitherT(checkPassword2(user, password))
      _    <- EitherT(checkSubscription(user))
      _    <- EitherT(checkUserStatus(user))
    } yield user

  authenticate("", "").value.flatMap({
    case Right(user)                  => IO(println(user))
    case Left(BannedUser)             => IO(println(s"Error! The user is banned"))
    case Left(WrongPassword)          => IO(println(s"Error! Wrong password"))
    case Left(ExpiredSubscription(_)) => IO(println(s"Error! Expired Subscription"))
    case Left(WrongUserName)          => IO(println(s"Error! WrongUserName"))
  })

  case class SecretDocument(name: String)
  def getDocument: IO[SecretDocument] = ???
  def destroyDocument: IO[Unit] = IO.unit

  type Count = Int

  val readSecretDocument: User => EitherT[IO, String, SecretDocument] = {
    val state: StateT[ReaderT[IO, User, *], Count, Either[String, SecretDocument]] =
      StateT[ReaderT[IO, User, *], Int, Either[String, SecretDocument]](currentAttemptsCount =>
        ReaderT[IO, User, (Count, Either[String, SecretDocument])](user =>
          if (currentAttemptsCount >= 3)
            destroyDocument.as((currentAttemptsCount, Left("Max attempts exceeded")))
          else if (user.isAdmin) getDocument.map(doc => (currentAttemptsCount, Right(doc)))
          else IO.pure((currentAttemptsCount + 1, Left("Access denied")))
        )
      )

    state.run(0).map(_._2).mapF(EitherT(_)).run
  }

}
