package bytes.tcp

import bytes._
import cats.effect.{Concurrent, IO, IOApp, Sync}
import cats.effect.std.Console
import cats.implicits._
import fs2._
import fs2.io.net.Network
import fs2.io.net.Socket

object TcpListenForBytes extends IOApp.Simple {

  private def handleClientConnection[F[_]: Sync: Console](socket: Socket[F]) = {
    val t = F.delay(System.currentTimeMillis())

    t.flatMap { t1 =>
      socket.reads
        .buffer(1.m)
        .compile
        .count
        .flatTap(len => F.println(s"len: $len"))
        .flatMap(_ => t.map(t2 => t2 - t1))
        .flatTap(time => F.println(s"ms: $time"))
        .as(Stream.empty)
    }
  }

  private def getAndCountBytes[F[_]: Network: Console: Concurrent: Sync]: F[Unit] =
    Network[F]
      .server(port = port.some)
      .evalMap(clientSocket => handleClientConnection[F](clientSocket))
      .parJoin(100)
      .compile
      .drain

  override def run: IO[Unit] = getAndCountBytes[IO]

}
