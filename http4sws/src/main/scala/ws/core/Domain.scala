package ws.core

import cats.data.Validated
import cats.syntax.all.*

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

case class ChatState(roomMembers: Map[Room, Set[User]])

object ChatState {

  def fresh: ChatState = apply(Map.empty)

  extension(state: ChatState) {

    /** reviewed, based on the implementation */

    /** TODO: to review */
    def userRooms: Map[User, Room] =
      state.roomMembers.flatMap { case (room, users) => users.map(user => user -> room) }

    def userExists(name: String): Boolean =
      state.roomMembers.exists { case (_, members) => members.exists(_.value == name) }

    def findRoom(user: User): Option[(Room, Set[User])] =
      state.roomMembers
        .find { case (room, users) => users.contains(user) }

    def withoutUser(user: User): ChatState =
      findRoom(user) match {
        case None                => state
        case Some((room, users)) => ChatState(state.roomMembers.updated(room, users - user))
      }

    def withUser(user: User, room: Room): ChatState = {
      val members = state.roomMembers.getOrElse(room, Set.empty) + user
      ChatState(state.roomMembers + (room -> members))
    }

    def usersSorted(room: Room): List[User] =
      state.roomMembers
        .get(room)
        .toList
        .flatMap(_.toList.sortBy(_.value))

    def metricsAsHtml: String =
      s"""<!Doctype html>
          |<title>Chat Server State</title>
          |<body>
          |<pre>Users: ${state.roomMembers.foldLeft(0) { case (total, (_, users)) => total + users.size }}</pre>
          |<pre>Rooms: ${state.roomMembers.keys.size}</pre>
          |<pre>Overview:
          |${state.roomMembers.keys.toList
          .map(room =>
            state.roomMembers
              .getOrElse(room, Set())
              .map(_.value)
              .toList
              .sorted
              .mkString(s"${room.value} Room Members:\n\t", "\n\t", "")
          )
          .mkString("Rooms:\n\t", "\n\t", "")}
          |</pre>
          |</body>
          |</html>
          """.stripMargin
  }

}
