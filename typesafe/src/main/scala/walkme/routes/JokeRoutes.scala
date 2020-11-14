package walkme.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import walkme.service.ForwardLogic
import walkme.service.ForwardLogic.RequestAJoke

import scala.concurrent.Future

class JokeRoutes(service: ActorRef[ForwardLogic.Command])(implicit val system: ActorSystem[_]) {
  // timeout for ask pattern
  private implicit val timeout =
    Timeout.create(system.settings.config.getDuration("jokes-app.routes.ask-timeout"))

  def jokeFromService: Future[String] = service.ask(a => RequestAJoke(a))
  
  val routes: Route = get {
    complete(jokeFromService)
  } 
  
}
