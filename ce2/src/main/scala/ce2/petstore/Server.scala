package ce2.petstore

import cats._
import cats.arrow.FunctionK
import cats.effect._
import cats.implicits._
import cats.tagless._
import cats.tagless.implicits._
import org.http4s._
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.Router

import scala.concurrent.duration._

object Server extends IOApp {

  def run(args: List[String]) =
    runR.use(_ => IO.never)

  def runR: Resource[IO, Unit] =
    for {
      rs <- routes
      _ <- server(Router("/" -> rs).orNotFound)
    } yield ()

  def server(app: HttpApp[IO]): Resource[IO, org.http4s.server.Server[IO]] =
    EmberServerBuilder
      .default[IO]
      .withPort(8080)
      .withHttpApp(app)
      .build

  def addExtraLatency[Alg[_[_]]: FunctorK, F[_]: Monad: Timer](extraLatency: FiniteDuration)(fa: Alg[F]): Alg[F] =
    fa.mapK(λ[FunctionK[F, F]](Timer[F].sleep(extraLatency) >> _))

  def routes: Resource[IO, HttpRoutes[IO]] =
    for {
      pets <- ServerResources.pets[IO].map(addExtraLatency(100.millis))
      orderRepo <- ServerResources.orderRepo[IO]
      orders = addExtraLatency(100.millis)(ServerResources.orders(pets, orderRepo))
    } yield Routes.pets[IO](pets) <+> Routes.orders[IO](orders)

}
