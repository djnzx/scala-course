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
import scala.concurrent.duration._
import sourcecode.FileName.{generate => fn}
import sourcecode.Line.{generate => ln}
import ws.core.ChatState
import ws.core.DebugThings
import ws.core.OutputMsg
import ws.core._

class WsHandler[F[_]: Async](
    protocol: Protocol[F],
    chatStateRef: Ref[F, ChatState[F]],
    publicTopic: Topic[F, OutputMsg])
    extends DebugThings[F] {

  def make(wsb: WebSocketBuilder2[F]): F[Response[F]] =
    for {
      userRef   <- Ref[F].of[Option[User]](None) // we need to know whether current user is authenticated to provide different error messages
      _         <- logF(s"created new Ref[Option[User]] for NEW incoming WS connection: $userRef")(ln, fn)
      userQueue <- Queue.unbounded[F, OutputMsg] // user's message queue to send him personal messages
      ws        <- wsb
                     .withOnClose(logF(s"conn $userRef CLOSED")(ln, fn))
                     .build(
                       send(userRef, userQueue),
                       receive(userRef, userQueue)
                     )
    } yield ws

  private def receive(userRef: Ref[F, Option[User]], userQueue: Queue[F, OutputMsg]): Pipe[F, WebSocketFrame, Unit] =
    _.evalTap(x => logF(x)(ln, fn))
      .map(InputFrame.parse)
      .evalTap(x => logF(x)(ln, fn))
      .map(InputMsg.apply)
      .evalTap(x => logF(x)(ln, fn))
      .evalMap(handle(userRef, userQueue))

  private def handle(userRef: Ref[F, Option[User]], userQueue: Queue[F, OutputMsg])(im: InputMsg): F[Unit] =
    im match {
      case InputMsg.Help            => ???
      case InputMsg.Login(userName) =>
        protocol
          .login(userRef, User(userName) -> UserState(userQueue))
          .flatTap { case (ms, log) => log.traverse_(s => logF(s)(ln, fn)) } // do log
          .flatMap { case (ms, log) => ms.map(_._1).traverse_(userQueue.offer) } // publish messages to private queue
      case InputMsg.Logout          =>
        userRef.get.flatMap {
          case None       => logF("nobody has logged in")(ln, fn)
          case Some(user) =>
            logF("logging out..." -> user)(ln, fn) >>
              chatStateRef.update(_.withoutUser(user)) >> // modify chat state
              userRef.update(_ => None) >>                // modify per-connection user status
              logF("...logged out" -> user)(ln, fn) >>
              userRef.get.flatMap(x => logF(x)(ln, fn)) >>
              chatStateRef.get.flatMap(x => logF(x.users)(ln, fn))
        }

      case InputMsg.InalidCommand(cmd)          => ???
      case InputMsg.PublicChatMessage(msg)      => ???
      case InputMsg.PrivateChatMessage(to, msg) => ???
      case InputMsg.InvalidMessage(details)     => ???
      case InputMsg.Disconnect                  => ???
      case InputMsg.ToDiscard                   => ???
    }

  private def send(userRef: Ref[F, Option[User]], userQueue: Queue[F, OutputMsg]): Stream[F, WebSocketFrame] = {
    val userStream: Stream[F, OutputMsg] = Stream.fromQueueUnterminated(userQueue)
    val publicStream: Stream[F, OutputMsg] = publicTopic.subscribeUnbounded

    (userStream merge publicStream)
      .evalTap(m => logF("user composite stream preparing to convert" -> m)(ln, fn))
      .mapFilter(msgToWsf)

  }

  private def msgToWsf(msg: OutputMsg): Option[WebSocketFrame] = msg match {
    case OutputMsg.KeepAlive          => WebSocketFrame.Ping().some
    case msg: OutputMsg.OutputMessage => WebSocketFrame.Text(msg.asJson.noSpaces).some
    case OutputMsg.ToDiscard          => None
  }

//  private def send(
//      uQueue: Queue[F, OutputMsg],
//      uRef: Ref[F, Option[User]]
//    ): Stream[F, WebSocketFrame] = {
//
//    def uStream: Stream[F, WebSocketFrame] =
//      Stream
//        .fromQueueUnterminated(uQueue)
//        .filter {
//          case DiscardMessage => false
//          case _              => true
//        }
//        .mapFilter(msgToWsf)
//
//    def topicStream: Stream[F, WebSocketFrame] =
//      Stream
//        .eval(protocol.getPublicTopic)
//        .flatMap { t =>
//          t.subscribeUnbounded
//            .evalFilter(filterMsgF(_, uRef))
//            .mapFilter(msgToWsf)
//        }
//
//    // here we build the stream We can't modify later
//    // since it's used to handle WS traffic
//    Stream(uStream, topicStream).parJoinUnbounded
//  }
//
//  private def filterMsgF(
//      msg: OutputMsg,
//      uRef: Ref[F, Option[User]]
//    ): F[Boolean] =
//    msg match {
//      case DiscardMessage      => false.pure[F]
//      case msg: MessageForUser =>
//        uRef.get.map {
//          case Some(u) => msg.isForUser(u)
//          case None    => false
//        }
//      case _                   => true.pure[F]
//    }
//
//  private def receive(
//      uQueue: Queue[F, OutputMsg],
//      uRef: Ref[F, Option[User]],
//      protocol: Protocol[F]
//    ): Pipe[F, WebSocketFrame, Unit] =
//    _.flatMap { wsf =>
////      logS("onReceive pipe" -> wsf)(ln, fn) ++
////        logS("ref" -> uRef)(ln, fn) ++
//      wsf match {
//        /** WebSocketFrame => List[OutputMsg] */
//        case WebSocketFrame.Text(text, _) =>
////            logS("onReceive text" -> text)(ln, fn) ++
//          Stream.evalSeq(lh.parse(uRef, text)) /// 1
//        case WebSocketFrame.Close(_)      => Stream.evalSeq(protocol.disconnect(uRef))
//        case wsf                          => pprint.log("onReceive other" -> wsf); Stream.empty
//      }
//    }
//      .evalMap { m =>
//        uRef.get.flatMap {
//          case Some(_) => protocol.sendPublicMessage(m)
//          case _       => uQueue.offer(m)
//        }
//      }
}
