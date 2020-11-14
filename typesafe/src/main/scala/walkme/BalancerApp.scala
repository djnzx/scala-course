package walkme

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Route
import walkme.routes.JokeRoutes
import walkme.service.ForwardLogic

import scala.util.{Failure, Success}

object BalancerApp extends App {

  private def startHttpServer(routes: Route)(implicit system: ActorSystem[_]) =
    Http()
      .newServerAt("localhost", 8080)
      .bind(routes)
      .onComplete {
        case Success(ServerBinding(la)) =>
          system.log.info("Server started at http://{}:{}/", la.getHostString, la.getPort)
        case Failure(ex) =>
          system.log.error("Server failed to start", ex)
          system.terminate()
      } (system.executionContext)

  val rootBehavior = Behaviors.setup[Nothing] { ctx =>
    val forwardLogic = ForwardLogic(ctx.system)
    val forwardLogicActor = ctx.spawn(forwardLogic, "ForwardJokeRequest")
    ctx.watch(forwardLogicActor)
    val jRoutes = new JokeRoutes(forwardLogicActor)(ctx.system)
    startHttpServer(jRoutes.routes)(ctx.system)
    Behaviors.empty
  }

  ActorSystem[Nothing](rootBehavior, "AkkaHttpServer")
}
