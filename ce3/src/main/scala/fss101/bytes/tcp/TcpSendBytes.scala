package fss101.bytes.tcp

import fss101.bytes._
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.MonadCancelThrow
import cats.effect.Sync
import cats.implicits._
import com.comcast.ip4s.SocketAddress
import fs2._
import fs2.io.net.Network

object TcpSendBytes extends IOApp.Simple {
  private val addr = SocketAddress(host, port)
  private val data: Array[Byte] = Array.fill(10.kb)('a'.toByte)
  private val chunk: Chunk[Byte] = Chunk.array(data)

  def client[F[_]: MonadCancelThrow: Network: Sync]: F[Unit] =
    Network[F]
      .client(addr)
      .use { socket =>
        // 1Gb ~ 10 Gbit
        (1 to 100.kb).toList.traverse_(_ => socket.write(chunk))
      }

  override def run: IO[Unit] = client[IO]

}
