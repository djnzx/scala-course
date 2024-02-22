package ws

import cats.effect.IO
import cats.effect.IOApp
import cats.implicits.catsSyntaxApplicativeId

/** - Websocket: [[https://blog.rockthejvm.com/websockets-in-http4s/]]
  * - UDP [[https://blog.rockthejvm.com/scala-fs2-udp/]]
  * - Load Balancer [[https://blog.rockthejvm.com/load-balancer/]]
  * - gRPC [[https://blog.rockthejvm.com/grpc-in-scala-with-fs2-scalapb/]]
  * - OAuth [[https://blog.rockthejvm.com/oauth-authentication-scala-http4s/]]
  * - VThreads [[https://blog.rockthejvm.com/ultimate-guide-to-java-virtual-threads/]]
  */
object App extends IOApp.Simple {

  override def run: IO[Unit] = ().pure[IO]
}
