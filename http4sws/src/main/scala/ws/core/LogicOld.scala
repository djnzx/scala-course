package ws.core

import cats.Applicative
import cats.Monad
import cats.data.Validated
import cats.data.Validated.Invalid
import cats.data.Validated.Valid
import cats.effect.kernel.Ref
import cats.implicits.*
import cats.parse.Parser
import cats.parse.Parser.char
import cats.parse.Rfc5234.alpha
import cats.parse.Rfc5234.sp
import cats.parse.Rfc5234.wsp

object LogicOld {

  def make[F[_]: Monad](
      protocol: Protocol[F]
    ): LogicHandler[F] =
    new LogicHandler[F] {
      def defaultRoom: Validated[String, Room] = Room("room123").valid

      override def parse(
          userRef: Ref[F, Option[User]],
          text: String
        ): F[List[OutputMsg]] =
        text.trim match {
          case ""  => DiscardMessage.pure[List].pure[F]
          case txt =>
            userRef.get.flatMap {
              case Some(u) => processText4Reg(u, txt, protocol)
              case None    =>
                defaultRoom match {
                  case Valid(room) => processText4UnReg(txt, protocol, userRef, room)
                  case Invalid(e)  => ParseError(None, e).pure[List].pure[F]
                }
            }
        }
    }

  private def processText4UnReg[F[_]: Monad](
      text: String,
      protocol: Protocol[F],
      userRef: Ref[F, Option[User]],
      room: Room
    ): F[List[OutputMsg]] =
    text.head match {
      case '/' =>
        parseToTextCommand(text).fold(
          _ => ParseError(None, "Characters after '/' must be between A-Z or a-z").pure[List].pure[F],
          {
            case TextCommand("/name", Some(n)) =>
              protocol.isUsernameInUse(n).flatMap {
                case true  => ParseError(None, "User name already in use").pure[List].pure[F]
                case false =>
                  protocol.register(n).flatMap {
                    case SuccessfulRegistration(u) =>
                      userRef
                        .update(_ => Some(u))
                        .flatMap(_ => protocol.enterRoom(u, room))
                        .map(ms => MessageToUser(u, "/help shows all available commands") :: ms)
                    case e @ ParseError(_, _)      => e.pure[List].pure[F]
                    case _                         => List.empty[OutputMsg].pure[F]
                  }
              }
            case _                             => UnsupportedCommand(None).pure[List].pure[F]
          }
        )
      case _   => Register(None).pure[List].pure[F]
    }

  private def processText4Reg[F[_]: Applicative](
      user: User,
      text: String,
      protocol: Protocol[F]
    ): F[List[OutputMsg]] =
    text.head match {
      case '/' =>
        parseToTextCommand(text).fold(
          _ => ParseError(None, "Characters after '/' must be between A-Z or a-z").pure[List].pure[F],
          v =>
            v match {
              case TextCommand("/name", Some(n))       => ParseError(Some(user), "You can't register again").pure[List].pure[F]
              case TextCommand("/room", Some(roomRaw)) =>
                Room.validate(roomRaw) match {
                  case Valid(room) => protocol.enterRoom(user, room)
                  case Invalid(e)  => List(ParseError(Some(user), e)).pure[F]
                }
              case TextCommand("/help", None)          => protocol.help(user).map(_.pure[List])
              case TextCommand("/rooms", None)         => protocol.listRooms(user)
              case TextCommand("/members", None)       => protocol.listMembers(user)
              case _                                   => UnsupportedCommand(Some(user)).pure[List].pure[F]
            }
        )
      case _   => protocol.chat(user, text)
    }

  private def commandParser: Parser[TextCommand] = {
    val l = (char('/').string ~ alpha.rep.string).string
    val r = sp *> alpha.rep.string

    ((l ~ r.?) <* wsp.rep.?).map((l, r) => TextCommand(l, r))
  }

  private def parseToTextCommand(value: String) = commandParser.parseAll(value)

}
