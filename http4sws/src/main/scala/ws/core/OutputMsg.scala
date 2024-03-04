package ws.core

sealed trait OutputMsg

object OutputMsg {

  /** this is alias for WebSocketFrame.Ping */
  case object KeepAlive extends OutputMsg

  /** has a string message */
  sealed abstract class OutputMessage(msg: String) extends OutputMsg

  object OutputMessage {

    /** will be sent personally to user */
    sealed abstract class PrivateMessage(msg: String) extends OutputMessage(msg)

    final case class Welcome(msg: String = "please login with /login, or /help") extends PrivateMessage(msg)
    final case class ParseError(user: Option[User], msg: String)                 extends PrivateMessage(msg)
    final case class CommandError(user: Option[User], msg: String)               extends PrivateMessage(msg)
    final case class NameIsOccupied(user: User)                                  extends PrivateMessage(s"${user.value} is occupied by someone else")
    final case class NeedToLogout(user: User)                                    extends PrivateMessage(s"${user.value} need to logout before login")
    final case class Help(msg: String)                                           extends PrivateMessage(msg)

    /** will be sent to the whole chat */
    sealed abstract class PublicMessage(msg: String) extends OutputMessage(msg)

    final case class UserLoggedIn(user: User)  extends PublicMessage(s"${user.value} joined the chat")
    final case class UserLoggedOut(user: User) extends PublicMessage(s"${user.value} left the chat")
  }

}
