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

  val program = for {
    publicTopic <- Topic[IO, OutputMsg]                                    // topic to allow broadcast our outgoing message
    state       <- Ref.of[IO, ChatState[IO]](ChatState.fresh(publicTopic)) // application state
    protocol    <- IO(Protocol.make[IO](state))                            // having state, we can make a protocol which is essentially f: InputMsg => F[OutputMsg] - TODO
    logic       <- IO(LogicHandlerOld.make[IO](protocol))                  // basically handler: InputMsg => OutputMsg - TODO: eliminate
    wsHandler = new WsHandler[IO](logic, protocol).make    // f: WebSocketBuilder2[F] => F[Response[F]]
    httpRoute = new Routes[IO].endpoints(state, wsHandler) // f: WebSocketBuilder2[F] => HttpApp[F]
    _           <- Stream.eval(Server.make[IO](httpRoute)).compile.drain   // http / ws stream (forever)
    // TODO: probably it's better to have one KeepAlive generator instead of N-connections, and send KeepAlive(Ping) to public topic
  } yield ()

  override def run: IO[Unit] = program

}
