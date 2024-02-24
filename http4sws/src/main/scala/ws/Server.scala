package ws

import cats.effect.kernel.Async
import com.comcast.ip4s.*
import fs2.io.net.Network
import org.http4s.HttpApp
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.websocket.WebSocketBuilder2

object Server {

  val host = host"0.0.0.0"
  val port = port"8080"

  /** to build a server we only need a http handler */
  def make[F[_]: Async: Network](
      f: WebSocketBuilder2[F] => HttpApp[F]
    ): F[Nothing] =
    EmberServerBuilder
      .default[F]
      .withHost(host)
      .withPort(port)
      .withHttpWebSocketApp(f)
      .build
      .useForever

}
