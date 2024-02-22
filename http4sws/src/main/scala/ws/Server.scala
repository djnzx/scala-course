package ws

import cats.effect.Ref
import cats.effect.kernel.Async
import cats.effect.std.Queue
import cats.syntax.all.*
import com.comcast.ip4s.*
import fs2.concurrent.Topic
import fs2.io.file.Files
import fs2.io.net.Network
import org.http4s.ember.server.EmberServerBuilder
import ws.core.{ChatState, InputMessage, OutputMsg, Protocol}

object Server {

  val host = host"0.0.0.0"
  val port = port"8080"

  def server[F[_]: Async: Files: Network](
      q: Queue[F, OutputMsg],
      t: Topic[F, OutputMsg],
      im: InputMessage[F],
      protocol: Protocol[F],
      stateRef: Ref[F, ChatState]
    ): F[Unit] =
    EmberServerBuilder
      .default[F]
      .withHost(host)
      .withPort(port)
      .withHttpWebSocketApp(wsb => new Routes().service(wsb, q, t, im, protocol, stateRef))
      .build
      .useForever
      .void

}
