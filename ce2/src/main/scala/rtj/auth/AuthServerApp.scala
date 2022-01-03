package rtj.auth

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import org.http4s.HttpApp
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

object AuthServerApp extends IOApp {

  val as: AuthService = AuthService()
  val ar: AuthRoutes[IO] = new AuthRoutes[IO](as)
  val app: HttpApp[IO] = ar.routes.orNotFound
  val ec: ExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(8))

  val server = BlazeServerBuilder[IO](ec)
    .bindLocal(8080)
    .withHttpApp(app)
    .resource
    .use(_ => IO.never)

  override def run(args: List[String]): IO[ExitCode] = server

}
