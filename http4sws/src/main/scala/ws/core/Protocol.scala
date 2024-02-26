package ws.core

import cats.Applicative
import cats.Monad
import cats.data.Validated.Invalid
import cats.data.Validated.Valid
import cats.effect.kernel.Ref
import cats.syntax.all.*

/** state manipulation:
  * {{{
  *   InputMsg => F[List[OutputMsg]]
  * }}}
  *
  * having output as a List[OutputMsg] enables:
  * {{{List.empty}}} - drop incoming message
  * {{{List(one)}}} - emit one message
  * {{{List(one, two)}}} - emit many message
  */
trait Protocol[F[_]] {
  def validate(name: String): F[OutputMsg]
  def isInUse(name: String): F[Boolean]
  def enterRoom(user: User, room: Room): F[List[OutputMsg]]
  def chat(user: User, text: String): F[List[OutputMsg]]
  def help(user: User): F[OutputMsg]
  def listRooms(user: User): F[List[OutputMsg]]
  def listMembers(user: User): F[List[OutputMsg]]
  def disconnect(uRef: Ref[F, Option[User]]): F[List[OutputMsg]]
}

object Protocol {

  def make[F[_]: Monad](stateRef: Ref[F, ChatState]): Protocol[F] =
    new Protocol[F] {
      // it only validates name, doesn't register
      override def validate(name: String): F[OutputMsg] =
        User.validate(name) match {
          case Valid(user)   => SuccessfulRegistration(user).pure[F]
          case Invalid(e) => ParseError(None, e).pure[F]
        }

      override def isInUse(name: String): F[Boolean] = stateRef.get.map(_.userExists(name))

      override def enterRoom(user: User, room: Room): F[List[OutputMsg]] =
        stateRef.get.flatMap { state =>
          state.userRooms.get(user) match {
            case Some(r) if r == room => MessageToUser(user, s"You are already in the ${room.value} room").pure[List].pure[F]
            case Some(room)           =>
              val leaveMessages = removeFromCurrentRoom(stateRef, user)
              val enterMessages = addToRoom(stateRef, user, room)
              for {
                leave <- leaveMessages
                enter <- enterMessages
              } yield leave ++ enter
            case None                 => addToRoom(stateRef, user, room)
          }
        }

      override def chat(user: User, text: String): F[List[OutputMsg]] =
        stateRef.get.flatMap { state =>
          state.userRooms.get(user) match {
            case Some(room) => broadcastMessage(state, room, ChatMessage(user, user, text))
            case None       => List(MessageToUser(user, "You are not currently in a room")).pure[F]
          }
        }

      val helpText =
        """Commands:
          | /help             - Show this text
          | /room             - Change to default/entry room
          | /room <room name> - Change to specified room
          | /rooms            - List all rooms
          | /members          - List members in current room
      """.stripMargin

      override def help(user: User): F[OutputMsg] = MessageToUser(user, helpText).pure[F]

      override def listRooms(user: User): F[List[OutputMsg]] =
        stateRef.get.map { state =>
          val roomList =
            state.roomMembers.keys
              .map(_.value)
              .toList
              .sorted
              .mkString("Rooms:\n\t", "\n\t", "")
          List(MessageToUser(user, roomList))
        }

      override def listMembers(user: User): F[List[OutputMsg]] =
        stateRef.get.map { state =>
          val memberList =
            state.userRooms.get(user) match {
              case Some(room) =>
                state
                  .usersSorted(room)
                  .map(_.value)
                  .mkString("Room Members:\n\t", "\n\t", "")
              case None       => "You are not currently in a room"
            }
          List(MessageToUser(user, memberList))
        }

      override def disconnect(uRef: Ref[F, Option[User]]): F[List[OutputMsg]] = // TODO: seems not clear
        uRef.get.flatMap {
          case Some(user) => removeFromCurrentRoom(stateRef, user)
          case None       => List.empty[OutputMsg].pure[F]
        }

    }

  private def broadcastMessage[F[_]: Applicative](
      state: ChatState,
      room: Room,
      om: OutputMsg
    ): F[List[OutputMsg]] =
    state.roomMembers
      .getOrElse(room, Set.empty[User])
      .map { u =>
        om match {
          case MessageToUser(user, msg)   => MessageToUser(u, msg)
          case ChatMessage(from, to, msg) => ChatMessage(from, u, msg)
          case _                          => DiscardMessage
        }
      }
      .toList
      .pure[F]

  private def addToRoom[F[_]: Monad](
      stateRef: Ref[F, ChatState],
      user: User,
      room: Room
    ): F[List[OutputMsg]] =
    stateRef
      .updateAndGet(_.withUser(user, room))
      .flatMap {
        broadcastMessage(
          _,
          room,
          MessageToUser(user, s"${user.value} has joined the ${room.value} room")
        )
      }

  private def removeFromCurrentRoom[F[_]: Monad](
      stateRef: Ref[F, ChatState],
      user: User
    ): F[List[OutputMsg]] =
    stateRef.get.flatMap { state =>
      state.userRooms.get(user) match {
        case Some(room) =>
          stateRef.update(_.withoutUser(user)) >>
            broadcastMessage(
              state,
              room,
              MessageToUser(user, s"${user.value} has left the ${room.value} room")
            )

        case None => List.empty[OutputMsg].pure[F]
      }
    }

}
