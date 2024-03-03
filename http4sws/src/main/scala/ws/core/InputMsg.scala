package ws.core

import org.http4s.websocket.WebSocketFrame

sealed trait InputMsg

object InputMsg {

  case object Help                                             extends InputMsg
  final case class Login(userName: String)                     extends InputMsg
  case object Logout                                           extends InputMsg
  final case class InalidCommand(cmd: String)                  extends InputMsg
  final case class PublicChatMessage(msg: String)              extends InputMsg
  final case class PrivateChatMessage(to: String, msg: String) extends InputMsg
  final case class InvalidMessage(details: String)             extends InputMsg
  case object Disconnect                                       extends InputMsg
  case object ToDiscard                                        extends InputMsg

  import InputFrame._

  def apply(frame: InputFrame): InputMsg = frame match {
    case TextFrame.CommandFrame.CommandValid("help", _)              => InputMsg.Help
    case TextFrame.CommandFrame.CommandValid("login", username :: _) => InputMsg.Login(username)
    case TextFrame.CommandFrame.CommandValid("logout", _)            => InputMsg.Logout
    case TextFrame.CommandFrame.CommandValid(cmd, _)                 => InputMsg.InalidCommand(cmd)
    case TextFrame.CommandFrame.CommandInvalid(raw)                  => InputMsg.InalidCommand(raw)
    case TextFrame.MessageFrame.PublicChatMessage(msg)               => InputMsg.PublicChatMessage(msg)
    case TextFrame.MessageFrame.PrivateChatMessage(to, msg)          => InputMsg.PrivateChatMessage(to, msg)
    case TextFrame.MessageFrame.InvalidMessage(details)              => InputMsg.InvalidMessage(details)
    case OtherFrame(WebSocketFrame.Close(_))                         => InputMsg.Disconnect
    case OtherFrame(WebSocketFrame.Pong(_))                          => InputMsg.ToDiscard
    case OtherFrame(_)                                               => InputMsg.ToDiscard
  }

}
