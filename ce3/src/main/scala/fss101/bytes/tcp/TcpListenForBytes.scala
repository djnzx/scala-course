package fss101.bytes.tcp

import fss101.bytes._
import cats.effect.{Concurrent, IO, IOApp, Sync}
import cats.effect.std.Console
import cats.implicits._
import fs2._
import fs2.io.net.Network
import fs2.io.net.Socket

object TcpListenForBytes extends IOApp.Simple {

  private def handleClientConnection[F[_]](socket: Socket[F])(implicit F: Sync[F], C: Console[F]) = {
    val t = F.delay(System.currentTimeMillis())

    t.flatMap { t1 =>
      socket.reads
        .buffer(1.mb)
        .compile
        .count
        .flatTap(len => C.println(s"len: $len"))
        .flatMap(_ => t.map(t2 => t2 - t1))
        .flatTap(time => C.println(s"ms: $time"))
        .as(Stream.empty)
    }
  }

  private def getAndCountBytes[F[_]: Network: Concurrent](implicit F: Sync[F], C: Console[F]): F[Unit] =
    Network[F]
      .server(port = port.some)
      .evalMap(clientSocket => handleClientConnection[F](clientSocket))
      .parJoin(100)
      .compile
      .drain


  implicitly[Sync[IO]]
  implicitly[Console[IO]]

  override def run: IO[Unit] = getAndCountBytes[IO]

}
