package ws

import cats.effect.MonadCancelThrow
import cats.effect.Ref
import cats.effect.Temporal
import cats.effect.kernel.Concurrent
import cats.effect.kernel.Ref
import cats.effect.std.Queue
import cats.syntax.all.*
import fs2.Pipe
import fs2.Stream
import fs2.concurrent.Topic
import fs2.io.file
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.Response
import org.http4s.server.websocket.WebSocketBuilder2
import org.http4s.websocket.WebSocketFrame
import scala.concurrent.duration.*
import ws.core.*

class WsHandler[F[_]: Concurrent: Temporal] {

  def make(
      q: Queue[F, OutputMsg],
      t: Topic[F, OutputMsg],
      lh: LogicHandler[F],
      protocol: Protocol[F],
    ): WebSocketBuilder2[F] => F[Response[F]] =
    wsb =>
      for {
        uRef   <- Ref.of[F, Option[User]](None)
        uQueue <- Queue.unbounded[F, OutputMsg]
        ws     <- wsb.build( // TODO: think how to implement in terms of Pipe[F, WebSocketFrame, WebSocketFrame]
                    send(t, uQueue, uRef),
                    receive(protocol, lh, uRef, q, uQueue)
                  )
      } yield ws

  private def msgToWsf(msg: OutputMsg): WebSocketFrame =
    msg match {
      case KeepAlive          => WebSocketFrame.Ping()
      case msg: OutputMessage => WebSocketFrame.Text(msg.asJson.noSpaces)
      case DiscardMessage     => ??? // TODO: think how to eliminate, filtered previously
    }

  private def filterMsg(
      msg: OutputMsg,
      userRef: Ref[F, Option[User]]
    ): F[Boolean] =
    msg match {
      case DiscardMessage      => false.pure[F]
      case msg: MessageForUser =>
        userRef.get.map {
          case Some(u) => msg.isForUser(u)
          case None    => false
        }
      case _                   => true.pure[F]
    }

  private def send(
      t: Topic[F, OutputMsg],
      messages: Queue[F, OutputMsg],
      uRef: Ref[F, Option[User]]
    ): Stream[F, WebSocketFrame] = {

    def uStream: Stream[F, WebSocketFrame] =
      Stream
        .fromQueueUnterminated(messages)
        .filter {
          case DiscardMessage => false
          case _              => true
        }
        .map(msgToWsf)

    def mainStream: Stream[F, WebSocketFrame] =
      t.subscribe(maxQueued = 1000)
        .evalFilter(filterMsg(_, uRef))
        .map(msgToWsf)

    Stream(uStream, mainStream).parJoinUnbounded
  }

  private def receive(
      protocol: Protocol[F],
      im: LogicHandler[F],
      uRef: Ref[F, Option[User]],
      q: Queue[F, OutputMsg],
      uQueue: Queue[F, OutputMsg]
    ): Pipe[F, WebSocketFrame, Unit] = { wsfs =>
    handleWebSocketStream(wsfs, im, protocol, uRef)
      .evalMap { m =>
        uRef.get.flatMap {
          case Some(_) => q.offer(m)
          case None    => uQueue.offer(m)
        }
      }
      .concurrently {
        Stream
          .awakeEvery(30.seconds)
          .as(KeepAlive)
          .foreach(uQueue.offer)
      }
  }

  private def handleWebSocketStream(
      wsf: Stream[F, WebSocketFrame],
      im: LogicHandler[F],
      protocol: Protocol[F],
      uRef: Ref[F, Option[User]]
    ): Stream[F, OutputMsg] =
    wsf.flatMap { sf =>
      Stream.evalSeq(
        sf match {
          case WebSocketFrame.Text(text, _) => im.parse(uRef, text)
          case WebSocketFrame.Close(_)      => protocol.disconnect(uRef)
          case f                            => MonadCancelThrow[F].raiseError(new RuntimeException(s"unexpected frame: $f"))
        }
      )
    }

}
