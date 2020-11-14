package akkahttp

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Route
import akkahttp.routes.UserRoutes
import akkahttp.service.UserService

import scala.util.{Failure, Success}

object AkkaHttpApp extends App {

  private def startHttpServer(routes: Route)(implicit system: ActorSystem[_]) =
    Http()
      .newServerAt("localhost", 8080)
      .bind(routes)
      .onComplete {
        case Success(ServerBinding(la)) =>
          system.log.info("Server online at http://{}:{}/", la.getHostString, la.getPort)
        case Failure(ex) =>
          system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
          system.terminate()
      } (system.executionContext)

  import akka.http.scaladsl.server.Directives._ // concat

  val root = Behaviors.setup[Nothing] { ctx: ActorContext[Nothing] =>
    /** services */
    val userService = UserService()              // apply call
    /** represent services as actors */
    val userServiceActor = ctx.spawn(userService, "UserRegistryActor")
    ctx.watch(userServiceActor)
    /** routes */
    val userRoutes = new UserRoutes(userServiceActor)(ctx.system)
    startHttpServer(userRoutes.routes)(ctx.system)
    Behaviors.empty
  }

  /** start actor system, and providing root behavior */
  ActorSystem[Nothing](root, "AkkaHttpServer")

}
