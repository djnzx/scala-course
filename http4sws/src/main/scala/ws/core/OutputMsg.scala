package ws.core

sealed trait OutputMsg

object OutputMsg {

  /** this is alias for WebSocketFrame.Ping */
  case object KeepAlive extends OutputMsg

  /** has a string message */
  sealed abstract class OutputMessage(msg: String) extends OutputMsg

  object OutputMessage {

    /** will be sent personally to user */
    sealed abstract class PrivateMsg(msg: String) extends OutputMessage(msg)

    final case class Welcome(msg: String = "Enter /login {name} to login and chat or /help to help") extends PrivateMsg(msg)
    final case class NameIsOccupied(user: User)                                                      extends PrivateMsg(s"${user.value} is occupied by someone else")
    final case class NeedToLogout(user: User)                                                        extends PrivateMsg(s"${user.value} need to logout before login")
    final case class PrivateMessage(msg: String)                                                     extends PrivateMsg(msg)

    /** will be sent to the whole chat */
    sealed abstract class PublicMsg(msg: String) extends OutputMessage(msg)

    final case class UserLoggedIn(user: User)               extends PublicMsg(s"${user.value} joined the chat")
    final case class UserLoggedOut(user: User)              extends PublicMsg(s"${user.value} left the chat")
    final case class PublicMessage(msg: String, from: User) extends PublicMsg(s"${from.value}: $msg")
  }

}
