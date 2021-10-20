package ce2.d07_resources

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource

import java.io.RandomAccessFile

class A4_FileBufferReader private (in: RandomAccessFile) {

  def readBuffer(offset: Long): IO[(Array[Byte], Int)] =
    IO {
      in.seek(offset)
      val buf = new Array[Byte](A4_FileBufferReader.bufferSize)
      val len = in.read(buf)
      (buf, len)
    }

  private def close: IO[Unit] = IO(in.close())

}

object A4_FileBufferReader extends IOApp {
  val bufferSize = 4096

  def makeResource(fileName: String): Resource[IO, A4_FileBufferReader] =
    Resource.make {
      IO(new A4_FileBufferReader(new RandomAccessFile(fileName, "r")))
    } { r =>
      r.close
    }

  override def run(args: List[String]): IO[ExitCode] = ???
}
