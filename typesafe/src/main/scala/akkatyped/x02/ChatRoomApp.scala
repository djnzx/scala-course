package akkatyped.x02

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

import akka.NotUsed
import akka.actor.typed.scaladsl.ActorContext
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.Terminated

sealed trait EventRoom
final case class RqSession(screenName: String, replyTo: ActorRef[EventSession]) extends EventRoom

sealed trait EventSession
final case class SessionGranted(handle: ActorRef[PostMessage]) extends EventSession
final case class SessionDenied(reason: String) extends EventSession
final case class MessagePosted(screenName: String, message: String) extends EventSession

sealed trait SessionCommand
final case class PostMessage(message: String) extends SessionCommand
private final case class NotifyClient(message: MessagePosted) extends SessionCommand

object ActorChatRoom {
  private final case class PublishSessionMessage(screenName: String, message: String) extends EventRoom

  // start with empty list
  def apply(): Behavior[EventRoom] = chatRoom(List.empty)

  private def chatRoom(sessions: List[ActorRef[SessionCommand]]): Behavior[EventRoom] =
    Behaviors.receive { (context, message: EventRoom) =>
      message match {
        case RqSession(screenName, client) =>
          // create a child actor for further interaction with the client
          val ses = context.spawn(
            session(context.self, screenName, client),
            name = URLEncoder.encode(screenName, StandardCharsets.UTF_8.name),
          )

          client ! SessionGranted(ses)
          chatRoom(ses :: sessions)

        case PublishSessionMessage(screenName, message) =>
          val notification = NotifyClient(MessagePosted(screenName, message))
          sessions.foreach(_ ! notification)
          Behaviors.same
      }
    }

  private def session(
      room: ActorRef[PublishSessionMessage],
      screenName: String,
      client: ActorRef[EventSession],
    ): Behavior[SessionCommand] =
    Behaviors.receiveMessage {
      case PostMessage(message) =>
        // from client, publish to others via the room
        room ! PublishSessionMessage(screenName, message)
        Behaviors.same
      case NotifyClient(message) =>
        // published from the room
        client ! message
        Behaviors.same
    }
}

object ActorGabbler {
  def apply(): Behavior[EventSession] = Behaviors.setup { context =>
    Behaviors.receiveMessage {
      case SessionGranted(handle) =>
        handle ! PostMessage("Hello World!")
        Behaviors.same
      case MessagePosted(screenName, message) =>
        context.log.info("message has been posted by '{}': {}", screenName, message)
        Behaviors.stopped
      case SessionDenied(_) => ???
    }
  }
}

object Main extends App {

  val init: Behavior[NotUsed] = Behaviors.setup { context =>
    val chatRoom = context.spawn(ActorChatRoom(), "chatroom")
    val gabbler = context.spawn(ActorGabbler(), "gabbler")

    // register for Terminated notification
    context.watch(gabbler)

    chatRoom ! RqSession("ol’ Gabbler", gabbler)

    // respond on gabbler termination (stop)
    Behaviors.receiveSignal { case (_, Terminated(_)) =>
      Behaviors.stopped
    }

  }

  ActorSystem(init, "root")
}
