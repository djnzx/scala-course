package ws.core

import cats.data.Validated
import cats.effect.std.Queue
import cats.implicits._

sealed trait NotificationType
object NotificationType {
  case object User extends NotificationType
  case object Room extends NotificationType
  case object Chat extends NotificationType
}

sealed trait HasStringValue {
  def value: String
}

object HasStringValue {

  def validate[A <: HasStringValue](
      entity: A,
      entityName: String
    ): Validated[String, A] =
    entity.valid
      .ensure(s"$entityName must be between 2 and 10 characters")(a => a.value.length >= 2 && a.value.length <= 10)

}

case class User(value: String) extends HasStringValue
object User {
  def validate(name: String): Validated[String, User] =
    HasStringValue.validate(new User(name), "User")
}

case class Room(value: String) extends HasStringValue
object Room {
  def validate(name: String): Validated[String, Room] =
    HasStringValue.validate(new Room(name), "Room")
}

case class UserState[F[_]](queue: Queue[F, OutputMsg])

case class ChatState[F[_]](
    users: Map[User, UserState[F]]
//    personalTopics: Map[User, Topic[F, OutputMsg]],
//    roomTopics: Map[Room, Topic[F, OutputMsg]],
//    members: Map[Room, Set[User]] // we do need that only to provide "statistics"
  )

object ChatState {

  def fresh[F[_]]: ChatState[F] = apply(Map.empty)

  implicit class ChatStateOps[F[_]](state: ChatState[F]) {

    /** reviewed, based on the implementation */
    def contains(user: User): Boolean = state.users.contains(user)

    def withUser(user: User, userState: UserState[F]): ChatState[F] = {
      val kv: (User, UserState[F]) = (user, userState)
      state.copy(users = state.users + kv)
    }

    def withoutUser(user: User): ChatState[F] =
      state.copy(users = state.users - user)

    def withUserIf(user: User, userState: UserState[F]): (ChatState[F], Boolean) =
      contains(user) match {
        case false => withUser(user, userState) -> true
        case true  => state                     -> false
      }
//
//    /** TODO: to review */
//    def userRooms: Map[User, Room] =
//      state.members.flatMap { case (room, users) => users.map(user => user -> room) }
//
//    def userExists(name: String): Boolean =
//      state.members.exists { case (_, members) => members.exists(_.value == name) }
//
//    def findRoom(user: User): Option[(Room, Set[User])] =
//      state.members
//        .find { case (room, users) => users.contains(user) }
//
//    def withoutUser(user: User): ChatState[F] =
//      findRoom(user) match {
//        case None                => state
//        case Some((room, users)) => state.copy(members = state.members.updated(room, users - user))
//      }
//
//    def withUser(user: User, room: Room): ChatState[F] = {
//      val members = state.members.getOrElse(room, Set.empty) + user
//      state.copy(members = state.members + (room -> members))
//    }
//
//    def usersSorted(room: Room): List[User] =
//      state.members
//        .get(room)
//        .toList
//        .flatMap(_.toList.sortBy(_.value))

    def metricsAsHtml: String = "metrics: TODO"
//      s"""<!Doctype html>
//          |<title>Chat Server State</title>
//          |<body>
//          |<pre>Users: ${state.members.foldLeft(0) { case (total, (_, users)) => total + users.size }}</pre>
//          |<pre>Rooms: ${state.members.keys.size}</pre>
//          |<pre>Overview:
//          |${state.members.keys.toList
//          .map(room =>
//            state.members
//              .getOrElse(room, Set())
//              .map(_.value)
//              .toList
//              .sorted
//              .mkString(s"${room.value} Room Members:\n\t", "\n\t", "")
//          )
//          .mkString("Rooms:\n\t", "\n\t", "")}
//          |</pre>
//          |</body>
//          |</html>
//          """.stripMargin
  }

}
