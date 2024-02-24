package ws

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Ref
import fs2.Stream
import fs2.concurrent.Topic
import org.http4s.HttpApp
import org.http4s.server.websocket.WebSocketBuilder2
import scala.concurrent.duration.*
import ws.core.*

object App extends IOApp.Simple {

  def httpServerStream(f: WebSocketBuilder2[IO] => HttpApp[IO]): Stream[IO, Nothing] =
    Stream.eval(Server.make[IO](f))

  def wsKeepAliveStream(t: Topic[IO, OutputMsg]): Stream[IO, Nothing] =
    Stream.awakeEvery[IO](10.seconds).as(KeepAlive).through(t.publish)

  val program = for {
    state    <- Ref.of[IO, ChatState](ChatState.fresh)        // application state (essentially, Map, wrapped into Ref)
    protocol <- IO(Protocol.make[IO](state))                  // having state, we can make a protocol which is essentially f: InputMsg => F[OutputMsg] - TODO
    topic    <- Topic[IO, OutputMsg]                          // topic to allow broadcast our outgoing message
    logic    <- IO(LogicHandlerOld.make[IO](protocol))        // basically handler: InputMsg => OutputMsg - TODO
    wsHandler = new WsHandler[IO](topic, logic, protocol).make // f: WebSocketBuilder2[F] => F[Response[F]]
    httpRoute = new Routes[IO].endpoints(state, wsHandler)     // f: WebSocketBuilder2[F] => HttpApp[F]
    s1 = httpServerStream(httpRoute)                           // stream, processing http / WS requests (forever)
    s2 = wsKeepAliveStream(topic)                              // stream, constantly publishing WebSocketFrame.Ping to Web Client // TODO: we have the same stream inside handle
    _        <- Stream(s1, s2).parJoinUnbounded.compile.drain // run all streams in parallel
  } yield ()

  override def run: IO[Unit] = program

}
