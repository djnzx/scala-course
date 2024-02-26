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
    topic: Topic[F, OutputMsg],
    lh: LogicHandlerOld[F],
    protocol: Protocol[F])
    extends DebugThings[F] {

  val wsfToInputFrame: WebSocketFrame => InputFrame = InputFrame.parse
  val ifToInputMsg: InputFrame => InputMsg = InputMsg.apply
  val processMsg: InputMsg => F[List[OutputMsg]] = _ => List.empty.pure[F] // TODO
  val outputMsgToWsf: OutputMsg => Option[WebSocketFrame] = msgToWsf

  def make(wsb: WebSocketBuilder2[F]): F[Response[F]] =
    for {
      uRef   <- Ref.of[F, Option[User]](None)
      _      <- logF(s"created new Ref[Option[User]] for NEW incoming WS connection: $uRef")(ln, fn)
      uQueue <- Queue.unbounded[F, OutputMsg]
      _      <- logF(wsb)(ln, fn)
      ws     <- wsb
                  .withFilterPingPongs(false)
                  .withOnClose(protocol.disconnect(uRef) >> logF(s"conn $uRef CLOSED")(ln, fn))
                  .build((wsfs: Stream[F, WebSocketFrame]) =>
                    wsfs.evalTap(x => logF(x)(ln, fn))
                      .map(wsfToInputFrame)
                      .evalTap(x => logF(x)(ln, fn))
                      .map(ifToInputMsg)
                      .evalTap(x => logF(x)(ln, fn))
                      .evalMap(processMsg)
                      .flatMap(Stream.emits)
                      .mapFilter(outputMsgToWsf)
//                      .concurrently(Stream.awakeEvery[F](10.seconds).as(KeepAlive).th)
                  )
    } yield ws

  def make_old(wsb: WebSocketBuilder2[F]): F[Response[F]] =
    for {
      uRef   <- Ref.of[F, Option[User]](None)
      _      <- uRef.get.flatMap(r => logF(s"created new Ref[Option[User]] for NEW incoming WS connection: ${r}")(ln, fn))
      uQueue <- Queue.unbounded[F, OutputMsg]
      _      <- logF(wsb)(ln, fn)
      ws     <- wsb
                  .withFilterPingPongs(false)
                  .withOnClose(logF("conn CLOSED")(ln, fn))
                  .build( // TODO: think how to implement in terms of Pipe[F, WebSocketFrame, WebSocketFrame]
                    send(uQueue, uRef),
                    receive(uQueue, uRef)
                  )
    } yield ws

  private def send(
      uQueue: Queue[F, OutputMsg],
      uRef: Ref[F, Option[User]]
    ): Stream[F, WebSocketFrame] = {

    def uStream: Stream[F, WebSocketFrame] =
      Stream
        .fromQueueUnterminated(uQueue)
        .filter {
          case DiscardMessage => false
          case _              => true
        }
        .mapFilter(msgToWsf)

    def topicStream: Stream[F, WebSocketFrame] =
      topic
        .subscribe(maxQueued = 1000)
        .evalFilter(filterMsgF(_, uRef))
        .mapFilter(msgToWsf)

    Stream(uStream, topicStream).parJoinUnbounded
  }

  private def filterMsgF(
      msg: OutputMsg,
      uRef: Ref[F, Option[User]]
    ): F[Boolean] =
    msg match {
      case DiscardMessage      => false.pure[F]
      case msg: MessageForUser =>
        uRef.get.map {
          case Some(u) => msg.isForUser(u)
          case None    => false
        }
      case _                   => true.pure[F]
    }

  private def msgToWsf(msg: OutputMsg): Option[WebSocketFrame] =
    msg match {
      case KeepAlive          => WebSocketFrame.Ping().some
      case msg: OutputMessage => WebSocketFrame.Text(msg.asJson.noSpaces).some
      case DiscardMessage     => None
    }

  private def wsKeepAliveStream: Stream[F, Nothing] =
    Stream.awakeEvery[F](30.seconds).as(KeepAlive).through(topic.publish)

  private def wsKeepAliveStream(q: Queue[F, OutputMsg]): Stream[F, Unit] =
    Stream.awakeEvery[F](30.seconds).as(KeepAlive).evalTap(q.offer).void

  private def receive(
      uQueue: Queue[F, OutputMsg],
      uRef: Ref[F, Option[User]],
    ): Pipe[F, WebSocketFrame, Unit] =
    _.flatMap { wsf =>
//      logS("onReceive pipe" -> wsf)(ln, fn) ++
//        logS("ref" -> uRef)(ln, fn) ++
      wsf match {
        /** WebSocketFrame => List[OutputMsg] */
        case WebSocketFrame.Text(text, _) =>
//            logS("onReceive text" -> text)(ln, fn) ++
          Stream.evalSeq(lh.parse(uRef, text)) /// 1
        case WebSocketFrame.Close(_)      => Stream.evalSeq(protocol.disconnect(uRef))
        case wsf                          => pprint.log("onReceive other" -> wsf); Stream.empty
      }
    }
      .evalMap { m =>
        uRef.get.flatMap {
          case Some(_) => topic.publish1(m).void
          case _       => uQueue.offer(m)
        }
      }
      .concurrently(wsKeepAliveStream)

}
