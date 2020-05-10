package pfps

import cats._
import cats.effect._
import cats.implicits._
import scala.util.control.NoStackTrace
//
//import cats.data.{Kleisli, OptionT}
//import cats.effect.Sync
//import cats.{ApplicativeError, Functor, MonadError}
//import cats.syntax.applicative._
//import cats.syntax.applicativeError._
//import cats.syntax.eq._
//import cats.syntax.functor._
//
////import cats.syntax.all._
////import cats.implicits._
//import cats._
//import cats.effect._
//import cats.implicits._

import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, Response}
import scala.util.control.NoStackTrace

object A037ErrorHandling extends App {
  case class Category(name: String)

  trait Random[F[_]] {
    def bool: F[Boolean]
    def int: F[Int]
  }

  object Random {
    implicit def syncInstance[F[_]: Sync]: Random[F] = new Random[F] {
      def int: F[Int]      = Sync[F].delay(scala.util.Random.nextInt(100))
      def bool: F[Boolean] = ???
//      def bool: F[Boolean] = int.map(_ % 2 === 0)
    }
  }

  trait Categories[F[_]] {
    def findAll: F[List[Category]]
  }

  sealed trait BusinessError extends NoStackTrace
  case object RandomError extends BusinessError

  class LiveCategories[F[_]: MonadError[*[_], Throwable]: Random] extends Categories[F] {
    def findAll: F[List[Category]] = implicitly[Random[F]].bool.ifM(
      List.empty[Category].pure[F],
      RandomError.raiseError[F, List[Category]] )
  }



  class Program[F[_]: Functor](categories: Categories[F]) {

//    def findAll: F[List[Category]] = category.maybeFindAll.map {
//      case Right(c) => c
//      case Left(RandomError) => List.empty[Category]
//    }

  }

  type ApThrow[F[_]] = ApplicativeError[F, Throwable]
  class SameProgram[F[_]: ApThrow](categories: Categories[F]) {

//    def findAll: F[List[Category]] = category.findAll.handleError {
//      case RandomError => List.empty[Category]
//    }

  }

  sealed trait UserError extends NoStackTrace
  final case class UserAlreadyExists(username: String) extends UserError
  final case class UserNotFound(username: String) extends UserError
  final case class InvalidUserAge(age: Int) extends UserError

  trait HttpErrorHandler[F[_], E <: Throwable] {
    def handle(routes: HttpRoutes[F]): HttpRoutes[F]
  }

  abstract class RoutesHttpErrorHandler[F[_], E <: Throwable] extends HttpErrorHandler[F, E] with Http4sDsl[F] {

    def A: ApplicativeError[F, E]

    def handler: E => F[Response[F]]

    def handle(routes: HttpRoutes[F]): HttpRoutes[F] =
      Kleisli { req =>
        OptionT {
          A.handleErrorWith(routes.run(req).value) { e =>
            A.map(handler(e))(x => Option(x))
          }
        }
      } // handle

  }

}
