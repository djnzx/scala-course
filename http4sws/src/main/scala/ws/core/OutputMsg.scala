package ws.core

sealed trait OutputMsg

/** it's used for filtering purposes internally */
case object DiscardMessage extends OutputMsg

/** it's convertable to WebSocketFrame */
sealed trait MessageWithPayload extends OutputMsg

/** this is alias for WebSocketFrame.Ping */
case object KeepAlive extends OutputMsg

/** has a string message */
sealed abstract class OutputMessage(msg: String) extends MessageWithPayload

/** here we put all parsing errors for input message */
final case class ParseError(user: Option[User], msg: String) extends OutputMessage(msg)

/** successful parse, unsupported command. TODO */
final case class UnsupportedCommand(user: Option[User]) extends OutputMessage("Unsupported command")

final case class Register(user: Option[User]) extends OutputMessage("Register your username with the following command:\n/name <username>")

/** successful registration */
final case class SuccessfulRegistration(user: User) extends OutputMessage(s"${user.value} entered the chat")

sealed abstract class MessageForUser(msg: String) extends OutputMessage(msg) {
  def isForUser(targetUser: User): Boolean
}

final case class MessageToUser(user: User, msg: String) extends MessageForUser(msg) {
  def isForUser(targetUser: User): Boolean = targetUser == user
}

final case class ChatMessage(from: User, to: User, msg: String) extends MessageForUser(msg) {
  def isForUser(targetUser: User): Boolean = targetUser == to
}
