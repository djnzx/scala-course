package akkahttp.service

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}

import scala.collection.immutable

final case class User(name: String, age: Int, countryOfResidence: String)
final case class Users(users: immutable.Seq[User])

object UserService {
  sealed trait Command
  // requests
  final case class GetUsers(sender: ActorRef[Users]) extends Command                           // GET all
  final case class CreateUser(user: User, sender: ActorRef[ActionPerformed]) extends Command   // POST 1
  final case class GetUser(name: String, sender: ActorRef[GetUserResponse]) extends Command    // GET 1
  final case class DeleteUser(name: String, sender: ActorRef[ActionPerformed]) extends Command // DELETE 1
  // responses
  final case class GetUserResponse(maybeUser: Option[User])                                     // GET 1 => 
  final case class ActionPerformed(description: String)                                         // POST 1, DELETE 1 =>

  // start user service with empty database
  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(users: Set[User]): Behavior[Command] = Behaviors.receiveMessage {
        
    case GetUsers(sender) =>
      sender ! Users(users.toSeq) // ActorRef[Users]
      Behaviors.same
      
    case CreateUser(user, sender) =>
      sender ! ActionPerformed(s"User ${user.name} created.")
      registry(users + user)

    case GetUser(name, replyTo) =>
      replyTo ! GetUserResponse(users.find(_.name == name))
      Behaviors.same

    case DeleteUser(name, replyTo) =>
      replyTo ! ActionPerformed(s"User $name deleted.")
      registry(users.filterNot(_.name == name))
      
  }
}
