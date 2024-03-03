package ws

import cats.effect.Async
import cats.effect.Ref
import cats.effect.kernel.Ref
import cats.effect.std.Queue
import cats.syntax.all.*
import fs2.Pipe
import fs2.Stream
import fs2.concurrent.Topic
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.Response
import org.http4s.server.websocket.WebSocketBuilder2
import org.http4s.websocket.WebSocketFrame
import scala.concurrent.duration.*
import sourcecode.FileName.{generate => fn}
import sourcecode.Line.{generate => ln}
import ws.core.*

class WsHandler[F[_]: Async](
    chatState: Ref[F, ChatState[F]],
    publicTopic: Topic[F, OutputMsg])
    extends DebugThings[F] {

  def make(wsb: WebSocketBuilder2[F]): F[Response[F]] =
    for {
      userRef   <- Ref.of[F, Option[User]](None) // we need to know whether current user is authenticated to provide different error messages
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
      .void

  private def send(userRef: Ref[F, Option[User]], userQueue: Queue[F, OutputMsg]): Stream[F, WebSocketFrame] = {
    val userStream: Stream[F, OutputMsg] = Stream.fromQueueUnterminated(userQueue)
    val publicStream: Stream[F, OutputMsg] = publicTopic.subscribeUnbounded

    (userStream merge publicStream)
      .evalTap(m => logF("user composite stream preparing to convert" -> m)(ln, fn))
      .mapFilter(msgToWsf)

  }

  private def msgToWsf(msg: OutputMsg): Option[WebSocketFrame] =
    msg match {
      case OutputMsg.KeepAlive               => WebSocketFrame.Ping().some
      case msg: OutputMsg.MessageWithPayload => WebSocketFrame.Text(msg.asJson.noSpaces).some
      case OutputMsg.DiscardMessage          => None
    }

  //  def make(wsb: WebSocketBuilder2[F]): F[Response[F]] =
//    for {
//      uRef   <- Ref.of[F, Option[User]](None)
//      _      <- logF(s"created new Ref[Option[User]] for NEW incoming WS connection: $uRef")(ln, fn)
//      uQueue <- Queue.unbounded[F, OutputMsg]
//      _      <- logF(wsb)(ln, fn)
//      ws     <- wsb
//                  .withFilterPingPongs(false)
//                  .withOnClose(protocol.disconnect(uRef) >> logF(s"conn $uRef CLOSED")(ln, fn))
//                  .build((wsfs: Stream[F, WebSocketFrame]) =>
//                    wsfs
//                      .evalTap(x => logF(x)(ln, fn))
//                      .map(wsfToInputFrame)
//                      .evalTap(x => logF(x)(ln, fn))
//                      .map(ifToInputMsg)
//                      .evalTap(x => logF(x)(ln, fn))
//                      .evalMap(processMsg)
//                      .flatMap(Stream.emits)
//                      .mapFilter(outputMsgToWsf)
////                      .concurrently(Stream.awakeEvery[F](10.seconds).as(KeepAlive).th)
//                  )
//    } yield ws

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
