package ws.core

import cats.data.Validated
import cats.effect.std.Queue
import cats.implicits._

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
    users: Map[User, UserState[F]],
    // TODO
//    rooms: Map[Room, Set[User]]
  )

object ChatState {

  def fresh[F[_]]: ChatState[F] = apply(Map.empty)

  implicit class ChatStateOps[F[_]](state: ChatState[F]) {

    private def contains(user: User): Boolean = state.users.contains(user)

    private def withUser0(user: User, userState: UserState[F]): ChatState[F] = {
      val kv: (User, UserState[F]) = (user, userState)
      state.copy(users = state.users + kv)
    }

    private def withoutUser0(user: User): ChatState[F] =
      state.copy(users = state.users - user)

    /** not atomic, should be wrapped into `Ref` */
    def withUser(user: User, userState: UserState[F]): (ChatState[F], Boolean) =
      contains(user) match {
        case false => withUser0(user, userState) -> true
        case true  => state                      -> false
      }

    /** not atomic, should be wrapped into `Ref` */
    def withoutUser(user: User): (ChatState[F], Boolean) =
      contains(user) match {
        case true  => withoutUser0(user) -> true
        case false => state              -> false
      }

    def findUserState(user: User): Option[UserState[F]] =
      state.users.get(user)

    def metricsAsHtml: String = "metrics: TODO"
  }

}
