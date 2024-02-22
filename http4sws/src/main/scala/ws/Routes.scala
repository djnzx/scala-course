package ws

import cats.effect.MonadCancelThrow
import cats.effect.Temporal
import cats.effect.kernel.Ref
import cats.effect.std.Queue
import cats.syntax.all.*
import fs2.Pipe
import fs2.Stream
import fs2.concurrent.Topic
import fs2.io.file
import fs2.io.file.Files
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.HttpApp
import org.http4s.HttpRoutes
import org.http4s.MediaType
import org.http4s.StaticFile
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Content-Type`
import org.http4s.server.websocket.WebSocketBuilder2
import org.http4s.websocket.WebSocketFrame
import scala.concurrent.duration.*
import ws.core.ChatState
import ws.core.DiscardMessage
import ws.core.InputMessage
import ws.core.KeepAlive
import ws.core.MessageForUser
import ws.core.OutputMsg
import ws.core.OutputMessage
import ws.core.Protocol
import ws.core.User

class Routes[F[_]: Files: Temporal] extends Http4sDsl[F] {

  private val path = fs2.io.file.Path(getClass.getClassLoader.getResource("chat.html").getFile)

  def service(
      wsb: WebSocketBuilder2[F],
      q: Queue[F, OutputMsg],
      t: Topic[F, OutputMsg],
      im: InputMessage[F],
      protocol: Protocol[F],
      stateRef: Ref[F, ChatState]
    ): HttpApp[F] =
    HttpRoutes
      .of[F] {
        case rq @ GET -> Root / "chat.html" =>
          StaticFile
            .fromPath(path, Some(rq))
            .getOrElseF(NotFound()) // file notfound

        case GET -> Root / "ws" =>
          for {
            uRef    <- Ref.of[F, Option[User]](None)
            omQueue <- Queue.unbounded[F, OutputMsg]
            ws      <- wsb.build(
                         send(t, omQueue, uRef),
                         receive(protocol, im, uRef, q, omQueue)
                       )
          } yield ws

        case GET -> Root / "metrics" =>
          stateRef.get
            .map(_.metricsAsHtml)
            .flatMap(html => Ok(html, `Content-Type`(MediaType.text.html)))

      }
      .orNotFound // is Url, other than chat.html requested

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
      im: InputMessage[F],
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
      im: InputMessage[F],
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
