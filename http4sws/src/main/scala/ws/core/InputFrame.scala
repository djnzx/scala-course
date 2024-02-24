package ws.core

import cats.parse.Parser0
import org.http4s.websocket.WebSocketFrame

/** the most general trait */
sealed trait InputFrame

object InputFrame {

  /** 1/2. text frame */
  sealed trait TextFrame extends InputFrame

  object TextFrame {

    /** 1/2. command frame */
    sealed trait CommandFrame extends TextFrame

    object CommandFrame {

      /** 1/2. command invalid */
      final case class CommandInvalid(raw: String) extends CommandFrame

      /** 2/2. command valid w/parameters */
      final case class CommandValid(cmd: String, params: List[String]) extends CommandFrame

    }

    /** 2/2. message frame */
    sealed trait MessageFrame extends TextFrame

    object MessageFrame {

      /** 1/3. public message */
      final case class PublicChatMessage(message: String) extends MessageFrame

      /** 2/3. private message */
      final case class PrivateChatMessage(to: String, message: String) extends MessageFrame

      /** 3/3. invalid message (too short, invalid user, etc) */
      final case class InvalidMessage(details: String) extends MessageFrame

    }

  }

  /** 2/2. other WS frame */
  final case class OtherFrame(wsf: WebSocketFrame) extends InputFrame

  object p {
    import TextFrame.CommandFrame._
    import TextFrame.MessageFrame._
    import TextFrame._
    import cats.parse.Parser
    import cats.parse.Parser.char
    import cats.parse.Rfc5234.alpha
    import cats.parse.Rfc5234.vchar
    import cats.parse.Rfc5234.wsp

    /** filter for valid commands, NOT IN USE */
    def isCommandValid(cmd: String): Boolean = Set(
      "help",
      "room",
      "rooms",
      "members",
    ).contains(cmd)

    /** any command (detected by `/` prefix) */
    val cmdParser: Parser[String] = char('/') *> alpha.rep.string

    /** valid command (filtered by `isCommandValid`), tests only, NOT IN USE */
    val validCmdParser: Parser[String] = cmdParser.filter(isCommandValid)

    /** list of parameters */
    val parametersParser: Parser0[List[String]] = (alpha.rep.string <* wsp.?).rep0

    /** command with optional parameters */
    val cmdWithParametersParser: Parser[(String, List[String])] = (cmdParser <* wsp.rep0) ~ parametersParser

    /** command with parameters filtered by `isCommandValid` */
    val totalCommandParser: Parser[CommandFrame] = cmdWithParametersParser
      .map {
        //
        case (cmd, params) => CommandValid(cmd, params)
        // NOT IN USE
//        case (cmd, params) if isCommandValid(cmd) => CommandValid(cmd, params)
//        case (cmd, _)                             => CommandInvalid(cmd)
      }

    /** private message target (detected by `@` prefix) */
    val targetParser: Parser[String] = char('@') *> alpha.rep.string

    /** message with `@target` */
    val privateMessageParser: Parser[(String, String)] = (targetParser <* wsp.rep) ~ (vchar | wsp).rep.string

    /** public message */
    val publicMessageParser: Parser[String] = (vchar | wsp).rep(2).string

    /** private or public message */
    val privateOrPublicMessageParser: Parser[MessageFrame] =
      privateMessageParser.map { case (to, msg) => PrivateChatMessage(to, msg) } |
        publicMessageParser.map(msg => PublicChatMessage(msg))

    /** command, private or public (without InvalidMessage) */
    val totalInputFrameParser: Parser[TextFrame] = totalCommandParser | privateOrPublicMessageParser

    /** fallback - wraps everthing into InvalidMessage */
    def fallback(raw: String): InvalidMessage = TextFrame.MessageFrame.InvalidMessage(raw)

    /** total TextFrame combination */
    def totalTextParser(text: String): TextFrame =
      totalInputFrameParser.parseAll(text) match {
        case Right(parsed) => parsed
        case _             => fallback(text)
      }

  }

  /** total: WebSocketFrame => InputFrame */
  def parse(wsf: WebSocketFrame): InputFrame = wsf match {
    case WebSocketFrame.Text(text, _) => p.totalTextParser(text)
    case wsf                          => OtherFrame(wsf)
  }

}
