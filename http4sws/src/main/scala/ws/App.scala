package ws

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Ref
import fs2.Stream
import fs2.concurrent.Topic
import org.http4s.HttpApp
import org.http4s.server.websocket.WebSocketBuilder2
import scala.concurrent.duration._
import sourcecode.FileName.{generate => fn}
import sourcecode.Line.{generate => ln}
import ws.core._

object App extends IOApp.Simple with DebugThings[IO] {

  private def mkWebServerStream(routes: WebSocketBuilder2[IO] => HttpApp[IO]): Stream[IO, Nothing] =
    Stream.eval(Server.make[IO](routes))

  private def mkWsKeepAliveStream(publicTopic: Topic[IO, OutputMsg]): Stream[IO, Nothing] =
    Stream
      .awakeEvery[IO](30.seconds)
      .as(OutputMsg.KeepAlive)
      .evalTap(m => logF("KeepAliveStream: generated message for publicTopic" -> m)(ln, fn))
      .through(publicTopic.publish)

  override def run: IO[Unit] = for {
    publicTopic                       <- Topic[IO, OutputMsg]                                // topic to allow broadcast our outgoing message
    chatState: Ref[IO, ChatState[IO]] <- Ref.of[IO, ChatState[IO]](ChatState.fresh)          // application state
    protocol = Protocol.make[IO](chatState)
    wsHandler = new WsHandler[IO](protocol, chatState, publicTopic).make _ // f: WebSocketBuilder2[F] => F[Response[F]]
    httpRoute = new Routes[IO].endpoints(chatState, wsHandler)             // f: WebSocketBuilder2[F] => HttpApp[F]
    s1 = mkWebServerStream(httpRoute)                                      // http / ws stream (forever)
    s2 = mkWsKeepAliveStream(publicTopic)                                  // sw KeepAlive sender
    _                                 <- Stream(s1, s2).parJoinUnbounded.compile.drain.start // run Streams in background
    x                                 <- IO.never[Unit]                                      // never terminate app
  } yield x

}
