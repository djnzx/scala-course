package akkahttp.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import akkahttp.service.{User, UserService, Users}
import akkahttp.service.UserService._

import scala.concurrent.Future

class UserRoutes(service: ActorRef[UserService.Command])(implicit val system: ActorSystem[_]) {

  import akkahttp.tools.JsonEncDec._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  // tie business logic implementation via messages
  def getUsers():               Future[Users]           = service.ask(a => GetUsers(a))
  def getUser(name: String):    Future[GetUserResponse] = service.ask(a => GetUser(name, a))
  def createUser(user: User):   Future[ActionPerformed] = service.ask(a => CreateUser(user, a))
  def deleteUser(name: String): Future[ActionPerformed] = service.ask(a => DeleteUser(name, a))

  val routes: Route =
    pathPrefix("users") {
      concat(
        // -------------------------
        pathEnd {
          concat(
            get { // GET all 
              complete(getUsers()) // WRAP to 200
            },
            post { // POST 1
              entity(as[User]) { user =>
                onSuccess(createUser(user)) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            })
        },
        // -------------------------
        path(Segment) { name: String =>
          concat(
            get {   // GET 1
              rejectEmptyResponse {
                onSuccess(getUser(name)) { response =>
                  complete(response.maybeUser)
                }
              }
            },
            delete { // DELETE 1
              onSuccess(deleteUser(name)) { performed =>
                complete((StatusCodes.OK, performed))
              }
            })
        })
      // -------------------------
    }
}
