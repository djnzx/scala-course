package ws

import cats.effect.Async
import cats.effect.Ref
import cats.effect.std.Queue
import cats.implicits._
import fs2.Pipe
import fs2.Stream
import fs2.concurrent.Topic
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.Response
import org.http4s.server.websocket.WebSocketBuilder2
import org.http4s.websocket.WebSocketFrame
import sourcecode.FileName.{generate => fn}
import sourcecode.Line.{generate => ln}
import ws.core.DebugThings
import ws.core.OutputMsg
import ws.core.OutputMsg.OutputMessage
import ws.core._

class WsHandler[F[_]: Async](
    protocolBuilder: Ref[F, Option[User]] => Protocol[F],
    publicTopic: Topic[F, OutputMsg])
    extends DebugThings[F] {

  def make(wsb: WebSocketBuilder2[F]): F[Response[F]] =
    for {
      userQueue <- Queue.unbounded[F, OutputMsg] // user's message queue to send him personal messages
      userRef   <- Ref[F].of[Option[User]](None) // we need to know whether current user is authenticated to provide different error messages
      protocol = protocolBuilder(userRef)
      _         <- logF(s"new protocol instance built for NEW incoming WS connection: $userRef")(ln, fn)
      ws        <- wsb
                     .withOnClose(logF(s"conn $userRef Closed")(ln, fn))
                     .build(
                       send(userQueue),
                       receive(protocol, userQueue)
                     )
    } yield ws

  private def send(userQueue: Queue[F, OutputMsg]): Stream[F, WebSocketFrame] = {
    val userStream: Stream[F, OutputMsg] = Stream.fromQueueUnterminated(userQueue) // personal messages
    val publicStream: Stream[F, OutputMsg] = publicTopic.subscribeUnbounded        // public messages

    (userStream merge publicStream)
      .evalTap(m => logF("per user stream" -> m)(ln, fn))
      .map(msgToWsf)
  }

  private def msgToWsf(msg: OutputMsg): WebSocketFrame = msg match {
    case OutputMsg.KeepAlive          => WebSocketFrame.Ping()
    case msg: OutputMsg.OutputMessage => WebSocketFrame.Text(msg.asJson.noSpaces)
  }

  private def receive(protocol: Protocol[F], userQueue: Queue[F, OutputMsg]): Pipe[F, WebSocketFrame, Unit] =
    _.evalTap(x => logF(x)(ln, fn))
      .map(InputFrame.parse)
      .evalTap(x => logF(x)(ln, fn))
      .map(InputMsg.apply)
      .evalTap(x => logF(x)(ln, fn))
      .evalMap(handle(protocol, userQueue))

  private def handle(protocol: Protocol[F], userQueue: Queue[F, OutputMsg])(im: InputMsg): F[Unit] = {

    def handleMsg: F[Protocol.Outcome] = im match {
      // modifying app/user state
      case InputMsg.Login(userName)             => protocol.login(User(userName), UserState(userQueue))
      case InputMsg.Logout                      => protocol.logout()
      case InputMsg.Disconnect                  => protocol.disconnect()
      // reading app/user state
      case InputMsg.Help                        => protocol.help // this response is based on the state
      case InputMsg.PublicChatMessage(msg)      => protocol.sendPublic(msg)
      case InputMsg.PrivateChatMessage(to, msg) => protocol.sendPrivate(msg, to)
      // aren't accessing app/user state
      case InputMsg.InvalidCommand(cmd)         => protocol.respond(s"command `$cmd` was wrong, /help for details")
      case InputMsg.InvalidMessage(details)     => protocol.respond(s"message `$details` was too short")
      case InputMsg.ToDiscard                   => protocol.ignore
    }

    /** send to based on the message type */
    def doSend(msg: OutputMessage): F[Unit] = msg match {
      case m: OutputMessage.PrivateMessage => userQueue.offer(m)
      case m: OutputMessage.PublicMessage  => publicTopic.publish1(m).void
    }

    handleMsg
      .flatTap { case (_, logs) => logs.traverse_(s => logF(s)(ln, fn)) } // do log
      .flatMap { case (msgs, _) => msgs.traverse_(doSend) }
  }

}
